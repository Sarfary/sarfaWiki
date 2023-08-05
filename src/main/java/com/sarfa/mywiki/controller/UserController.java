package com.sarfa.mywiki.controller;

import com.sarfa.mywiki.req.UserLoginReq;
import com.sarfa.mywiki.req.UserQueryReq;
import com.sarfa.mywiki.req.UserResetPasswordReq;
import com.sarfa.mywiki.req.UserSaveReq;
import com.sarfa.mywiki.resp.CommonResp;
import com.sarfa.mywiki.resp.PageResp;
import com.sarfa.mywiki.resp.UserLoginResp;
import com.sarfa.mywiki.resp.UserQueryResp;
import com.sarfa.mywiki.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     *
     * @param req 分页 ，name模糊查询，分类查询, id查询
     * @return CommonResp{
     *   "success":
     *   "message":
     *   "content": {}
     *   }
     *
     */
    @GetMapping("/list")
    public CommonResp<PageResp<UserQueryResp>> list(@Valid UserQueryReq req){
        CommonResp<PageResp<UserQueryResp>> objectCommonResp = new CommonResp<>();
        PageResp<UserQueryResp> list= userService.list(req);
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }
    /**
     *
     * @param req 新增，更新的数据
     * @return CommonResp
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp objectCommonResp = new CommonResp<>();
        userService.save(req);
        return objectCommonResp;

    }

    /**
     *
     * @param req 重置密码
     * @return
     */
    @PostMapping("/resetPassword")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp objectCommonResp = new CommonResp<>();
        userService.resetPassword(req);
        return objectCommonResp;
    }

    /**
     *
     * @param req 登录
     * @return
     */
    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> objectCommonResp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);
        objectCommonResp.setContent(userLoginResp);
        return objectCommonResp;
    }
    /**
     *
     * @param id 根据id删除数据
     * @return CommonResp
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp objectCommonResp = new CommonResp<>();
        userService.delete(id);
        return objectCommonResp;
    }

}
