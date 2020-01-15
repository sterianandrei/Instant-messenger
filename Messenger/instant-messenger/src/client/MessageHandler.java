package client;

public class MessageHandler
{
    public enum MESSAGE_TYPE {
        LOGIN,
        LOG_SUCCS,
        LOG_FAIL,
        NEW_USR,
        NEW_MSG,
        ONL_USR,
        OFF_USR,
        ERR
    }

    public static MESSAGE_TYPE decodeMessage(String line)
    {
        if (line.startsWith("LOGIN"))
            return MESSAGE_TYPE.LOGIN;
        else if (line.startsWith("LOGSUCCESS"))
            return MESSAGE_TYPE.LOG_SUCCS;
        else if (line.startsWith("LOGERR"))
            return MESSAGE_TYPE.LOG_FAIL;
        else if (line.startsWith("NEWUSER"))
            return MESSAGE_TYPE.NEW_USR;
        else if (line.startsWith("MESSAGE"))
            return MESSAGE_TYPE.NEW_MSG;
        else if (line.startsWith("ONLNUSR"))
            return MESSAGE_TYPE.ONL_USR;
        else if (line.startsWith("OFFUSR"))
            return MESSAGE_TYPE.OFF_USR;
        return MESSAGE_TYPE.ERR;

    }

}
