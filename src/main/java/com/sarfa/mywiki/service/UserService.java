package com.sarfa.mywiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sarfa.mywiki.aspect.LogAspect;
import com.sarfa.mywiki.domain.User;
import com.sarfa.mywiki.domain.UserExample;
import com.sarfa.mywiki.exception.BusinessException;
import com.sarfa.mywiki.exception.BusinessExceptionCode;
import com.sarfa.mywiki.mapper.UserMapper;
import com.sarfa.mywiki.req.UserQueryReq;
import com.sarfa.mywiki.req.UserSaveReq;
import com.sarfa.mywiki.resp.UserQueryResp;
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
public class UserService {
    //    @Autowired 也可以用这个来注入，这个是springboot自带的
    //打印日志
    private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);
    @Resource//这个是jdk自带的
    private UserMapper userMapper;


    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }

        //分页
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        PageResp<UserQueryResp> objectPageResp = new PageResp<UserQueryResp>();
        objectPageResp.setTotal(pageInfo.getTotal());
        objectPageResp.setList(list);
        return objectPageResp;

    }

    @Resource
    private SnowFlake snowFlake;

    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(user.getId())) {
            //id为空，是新增
            //首先查看该loginName是否存在
            if(ObjectUtils.isEmpty(selectByLoginName(user.getLoginName()))){
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            }
            else {
                //用户名已存在
               throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }


        } else {
            //id不为空，是更新
            int a = userMapper.updateByPrimaryKey(user);
            //显示是否执行
            // LOG.info("{}",a);

        }

    }
    public void delete(Long id){
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String loginName){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if(userList.isEmpty()){
            return null;
        }
        else{
            return userList.get(0);
        }
    }



}
