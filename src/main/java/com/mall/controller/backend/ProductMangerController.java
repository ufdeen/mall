package com.mall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.Product;
import com.mall.pojo.User;
import com.mall.service.IFileService;
import com.mall.service.IProductService;
import com.mall.service.IUserService;
import com.mall.util.PropertiesUtil;
import com.mall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


@Controller
@RequestMapping("/manager/product/")
public class ProductMangerController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    /**
     * 判断用户是否登录且是否为管理员
     * */
    private ServerResponse managerJudge(User user){
        if(user == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前未登录,进行强制登录");
        }
        if(!iUserService.checkAdminRole(user).isSuccess()){
            return  ServerResponse.createByErrorMessage("非管理员用户，无法登录");
        }

        return  null;
    }


    @RequestMapping("saveOrUpdateProduct.do")
    @ResponseBody
    public ServerResponse saveOrUpdateProduct(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = managerJudge(user);
        if(serverResponse!=null){
            return serverResponse;
        }
        return  iProductService.saveOrUpdateProduct(product);
    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session,Integer productId,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = managerJudge(user);
        if(serverResponse!=null){
            return serverResponse;
        }

        return  iProductService.setSaleStatus(productId,status);

    }

    @RequestMapping("manager_product_detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> managerProductDetail(HttpSession session,Integer productId ){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = managerJudge(user);
        if(serverResponse!=null){
            return serverResponse;
        }

        return iProductService.managerProductDetail(productId);

    }

    @RequestMapping("manager_product_list.do")
    @ResponseBody
    public ServerResponse<PageInfo> managerProductList(HttpSession session, @RequestParam(value = "pageNo",defaultValue = "1")  Integer pageNo, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = managerJudge(user);
        if(serverResponse!=null){
            return serverResponse;
        }

        return iProductService.managerProductList(pageNo,pageSize);
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> productSearch(HttpSession session,String productName,Integer productId, @RequestParam(value = "pageNo",defaultValue = "1")  Integer pageNo, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = managerJudge(user);
        if(serverResponse!=null){
            return serverResponse;
        }

        return iProductService.productSearch(productName,productId,pageNo,pageSize);
    }

    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = managerJudge(user);
        if(serverResponse!=null){
            return serverResponse;
        }
        String path = session.getServletContext().getRealPath("upload");
        String filename = iFileService.upload(path,file);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+filename;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri",filename);
        fileMap.put("url",url);

        return  ServerResponse.createBySuccess(fileMap);

    }


    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file,HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","当前未登录");
        }
        if(!iUserService.checkAdminRole(user).isSuccess()){
            resultMap.put("success",false);
            resultMap.put("msg","当前非管理员用户");
        }

        String path = session.getServletContext().getRealPath("upload");
        String filename = iFileService.upload(path,file);
        if(filename == null ){
            resultMap.put("success",false);
            resultMap.put("msg","上传失败");
        }else{
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+filename;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        }
        return  resultMap;
    }
}
