package com.sarfa.mywiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sarfa.mywiki.aspect.LogAspect;
import com.sarfa.mywiki.domain.Category;
import com.sarfa.mywiki.domain.CategoryExample;
import com.sarfa.mywiki.mapper.CategoryMapper;
import com.sarfa.mywiki.req.CategoryQueryReq;
import com.sarfa.mywiki.req.CategorySaveReq;
import com.sarfa.mywiki.resp.CategoryQueryResp;
import com.sarfa.mywiki.resp.PageResp;
import com.sarfa.mywiki.util.CopyUtil;
import com.sarfa.mywiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {
    //    @Autowired 也可以用这个来注入，这个是springboot自带的
    //打印日志
    private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);
    @Resource//这个是jdk自带的
    private CategoryMapper categoryMapper;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        PageResp<CategoryQueryResp> objectPageResp = new PageResp<>();
        objectPageResp.setTotal(pageInfo.getTotal());
        objectPageResp.setList(list);
//        List<CategoryResp> respList = new ArrayList<>();
//        for (Category category:categoryList) {
////            CategoryResp categoryResp = new CategoryResp();
////            BeanUtils.copyProperties(category,categoryResp);
        //      单体复制
//            CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class);
//            respList.add(categoryResp);
//        }
        //列表复制
        return objectPageResp;

    }

    @Resource
    private SnowFlake snowFlake;

    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(category.getId())) {
            //id为空，是新增
            category.setId(snowFlake.nextId());
            if(ObjectUtils.isEmpty(category.getParent())){
                category.setParent(0L);
            }

            categoryMapper.insert(category);
        } else {
            //id不为空，是更新
            int a = categoryMapper.updateByPrimaryKey(category);
            //显示是否执行
            // LOG.info("{}",a);

        }

    }
    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }

}
