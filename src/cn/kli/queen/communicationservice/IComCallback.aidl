package cn.kli.queen.communicationservice;

import cn.kli.queen.communicationservice.ComMessage;

interface IComCallback 
{
    void onSendComplete(in ComMessage msg);
}