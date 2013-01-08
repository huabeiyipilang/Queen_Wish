package cn.kli.queen.communicationservice;

import cn.kli.queen.communicationservice.ComMessage;

interface ICommunication 
{
    int sendMessage(in ComMessage msg);
}