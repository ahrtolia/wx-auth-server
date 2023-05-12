package org.ahrtolia.controller;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import org.ahrtolia.service.OutNewsCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="https://github.com/007gzs">007</a>
 */
@RestController
@RequestMapping("/notify")
public class WechatNotifyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected OutNewsCardService outNewsCardService;

    @RequestMapping("/testServlet")
    @ResponseBody
    public String wxGZHGetMsg(HttpServletRequest request){

        // 获取随机数
        String echostr = request.getParameter("echostr");
        // 加密签名
        String signature = request.getParameter("signature");
        //随机数
        String nonce = request.getParameter("nonce");
        //时间戳
        String timestamp = request.getParameter("timestamp");
        verifyUrl(signature, timestamp, nonce);
        return request.getParameter("echostr");
    }

    public static boolean verifyUrl(String msgSignature, String timeStamp, String nonce) {
        String signature = getSHA1("lishangzhenxuan", timeStamp, nonce);
        return signature.equals(msgSignature);
    }

    public static String getSHA1(String token, String timestamp, String nonce) {
        try {
            String[] array = new String[]{token, timestamp, nonce};
            StringBuffer sb = new StringBuffer();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 3; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
