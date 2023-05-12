package org.ahrtolia.service;

import com.google.gson.Gson;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.ahrtolia.bean.Gift;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @create 2023/5/11 15:33:39
 * @Author by zhaoxiaodong
 */
@Service
public class OutNewsCardService {

    public WxMpXmlOutNewsMessage getNewsMessage(String msg) {

        WxMpXmlMessage wxMessage = WxMpXmlMessage.fromXml(msg);

        String giftStr = "";

        if (wxMessage.getEventKey().contains("qrscene_")) {
            giftStr = wxMessage.getEventKey().replaceAll("qrscene_", "");
        } else {
            giftStr = wxMessage.getEventKey();
        }

        Gson gson = new Gson();
        Gift gift = gson.fromJson(giftStr, Gift.class);
        if (gift == null) {
            return null;
        }

        WxMpXmlOutNewsMessage wxMpXmlOutNewsMessage = new WxMpXmlOutNewsMessage();
        WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();

        item.setPicUrl("http://wx.qlogo.cn/mmopen/qjQsccxa2ibLk1ycsSbD9oHnOpmLCxicODfErVqK4gCCMiaXsA9zRATUQA3twvian7DdZM6icrHbQRTn7bEGBB2vHt6GW6NUjZBNX/64");
        item.setUrl(gift.getURL());
        item.setTitle(gift.getName());
        item.setDescription(gift.getName());

        wxMpXmlOutNewsMessage.addArticle(item);

        wxMpXmlOutNewsMessage.setFromUserName(wxMessage.getToUser());
        wxMpXmlOutNewsMessage.setToUserName(wxMessage.getFromUser());
        wxMpXmlOutNewsMessage.setCreateTime(System.currentTimeMillis());

        return wxMpXmlOutNewsMessage;
    }

}
