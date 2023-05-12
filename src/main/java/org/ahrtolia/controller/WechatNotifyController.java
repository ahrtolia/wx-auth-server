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
import java.util.Arrays;

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
    public String handlePublicMsg(HttpServletRequest request) throws Exception {
        logger.info("进入推送消息方法");
        // 获得微信端返回的xml数据
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        is = request.getInputStream();
        isr = new InputStreamReader(is, "utf-8");
        br = new BufferedReader(isr);
        String str;
        StringBuilder returnXml = new StringBuilder();
        while ((str = br.readLine()) != null) {
            //返回的是xml数据
            returnXml.append(str);
        }

        WxMpXmlMessage wxMessage = WxMpXmlMessage.fromXml(returnXml.toString());
        // 区分消息类型
        String msgType = wxMessage.getMsgType();
        //场景信息
        String eventKey = wxMessage.getEncrypt();
        // 普通消息
        if ("text".equals(msgType)) { // 文本消息
            // todo 处理文本消息
        } else if ("image".equals(msgType)) { // 图片消息
            // todo 处理图片消息
        } else if ("voice".equals(msgType)) { //语音消息
            // todo 处理语音消息
        } else if ("video".equals(msgType)) { // 视频消息
            // todo 处理视频消息
        } else if ("shortvideo".equals(msgType)) { // 小视频消息
            // todo 处理小视频消息
        } else if ("location".equals(msgType)) { // 地理位置消息
            // todo 处理地理位置消息
        } else if ("link".equals(msgType)) { // 链接消息
            // todo 处理链接消息
        }
        // 事件推送
        else if ("event".equals(msgType)) { // 事件消息
            // 区分事件推送

            WxMpXmlOutNewsMessage wxMpXmlOutNewsMessage = outNewsCardService.getNewsMessage(returnXml.toString());

            String event = wxMessage.getEvent();
            logger.error("区分事件推送:" + event);
            if ("subscribe".equals(event)) { // 订阅事件 或 未关注扫描二维码事件

                logger.info("扫描二维码订阅");

                if (wxMpXmlOutNewsMessage == null) {
                    return getErrorWxMpXmlOutTextMessage(wxMessage).toXml();
                }
                return wxMpXmlOutNewsMessage.toXml();

            } else if ("unsubscribe".equals(event)) { // 取消订阅事件
                // todo 处理取消订阅事件
            } else if ("SCAN".equals(event)) { // 已关注扫描二维码事件
                logger.info("扫描二维码");

                if (wxMpXmlOutNewsMessage == null) {
                    return getErrorWxMpXmlOutTextMessage(wxMessage).toXml();
                }
                return wxMpXmlOutNewsMessage.toXml();
            } else if ("LOCATION".equals(event)) { // 上报地理位置事件
                // todo 处理上报地理位置事件
            } else if ("CLICK".equals(event)) { // 点击菜单拉取消息时的事件推送事件
                // todo 处理点击菜单拉取消息时的事件推送事件
            } else if ("VIEW".equals(event)) { // 点击菜单跳转链接时的事件推送
                // todo 处理点击菜单跳转链接时的事件推送
            }
        }
        return null;
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

    public WxMpXmlOutTextMessage getErrorWxMpXmlOutTextMessage(WxMpXmlMessage wxMessage) {
        WxMpXmlOutTextMessage wxMpXmlOutTextMessage = new WxMpXmlOutTextMessage();
        wxMpXmlOutTextMessage.setFromUserName(wxMessage.getToUser());
        wxMpXmlOutTextMessage.setToUserName(wxMessage.getFromUser());
        wxMpXmlOutTextMessage.setCreateTime(System.currentTimeMillis());
        wxMpXmlOutTextMessage.setContent("二维码已过期");
        return wxMpXmlOutTextMessage;
    }

}
