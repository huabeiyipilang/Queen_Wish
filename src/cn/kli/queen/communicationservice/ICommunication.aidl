package cn.kli.queen.communicationservice;

import cn.kli.queen.communicationservice.ComMessage;
import cn.kli.queen.communicationservice.IComCallback;

interface ICommunication 
{
    void sendMessage(in ComMessage msg);
    void registerForComState(in IComCallback callback);
    void unRegisterForComState(in IComCallback callback);
}