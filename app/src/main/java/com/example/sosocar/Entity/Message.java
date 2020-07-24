package com.example.sosocar.Entity;

public class Message {
    public final  static int Isend=0;//该消息是否是我发送的
    public final static int heSend=1;//该消息是否是对方发送的
    public final static int recentChatTime=2;//该消息显示最近一次聊天的时间
    private String messageContent;//消息内容
    private String latestChatTime;
    private  int type=Message.Isend;

    public Message(String content, int type) {
        if(type!=Message.recentChatTime){
            latestChatTime=null;
            this.messageContent = content;
        }
        else {
            this.latestChatTime=content;
            messageContent=null;
        }
        this.type = type;
    }


    public String getContent() {
        if(type==Message.recentChatTime)
            return latestChatTime;
        else return  messageContent;
    }


    public int getType() {
        return type;
    }

}