package com.saiyi.gymequipment.common.ble;

import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.utils.HexDump;

/**
 * Created by JingNing on 2018-07-19 14:28
 */
public class BleDataHandler {

    /**
     * 指令常量
     */
    public static class CmdConstans {

        /**
         * 指令头标识
         */
        public static final byte[] HEAD_CMD = new byte[]{0x55, 0x01};

        /**
         * 运动结束
         */
        public static final byte MOTION_END = (byte) 0XA7;

        /**
         * 设备广播过来的运动数据
         */
        public static final byte NOTITY_MOTION_DATA = (byte) 0XA8;

        /**
         * 运动结果
         */
        public static final byte MOTION_RESULET = (byte) 0XA9;

        /**
         * 指令结束标识
         */
        public static final byte[] END_CMD = new byte[]{0X0D};
    }

    private AnalyseDataListener analyseDataListener;

    public void setAnalyseDataListener(AnalyseDataListener analyseDataListener) {
        this.analyseDataListener = analyseDataListener;
    }

    /**
     * 解析数据
     *
     * @param data 收到完整数据
     */
    public void analyseData(byte[] data) {
        //检查头尾标识
        if (!checkEndCmd(data) || !checkStartCmd(data)) {
            if (analyseDataListener != null) {
                analyseDataListener.notifyDataErr();
            }
            return;
        }
        //进行异或校验
        if (!checkXor(data)) {
            if (analyseDataListener != null) {
                analyseDataListener.notifyDataErr();
            }
            return;
        }
        //判断命令类型
        if (getCmdType(data) == CmdConstans.NOTITY_MOTION_DATA) {
            //获取到运动数据
            int port = HexDump.bytesToInt(getPort(data));
            int frequency = HexDump.bytesToInt(getFrequency(data));
            int time = HexDump.bytesToInt(getTime(data));
            int calorie = HexDump.bytesToInt(getCalorie(data));
            int frec = HexDump.bytesToInt(getFrec(data));
            if (analyseDataListener != null) {
                analyseDataListener.notifyMotionData(port, frequency, time, calorie, frec);
            }
        }else if(getCmdType(data) == CmdConstans.MOTION_RESULET){
            //运动结果（在运动结束后，设备持续发送一段运动结果）
            int port = HexDump.bytesToInt(getPort(data));
            int frequency = HexDump.bytesToInt(getFrequency(data));
            int time = HexDump.bytesToInt(getTime(data));
            int calorie = HexDump.bytesToInt(getCalorie(data));
            int frec = HexDump.bytesToInt(getFrec(data));
            if (analyseDataListener != null) {
                analyseDataListener.notifyResult(port, frequency, time, calorie, frec);
            }
        }else if (getCmdType(data) == CmdConstans.MOTION_END) {
            //运动已结束
            if (analyseDataListener != null) {
                analyseDataListener.notifyEnd();
            }

        }
    }

    private byte[] getPort(byte[] data) {
        if (data != null && data.length > CmdConstans.HEAD_CMD.length + 1) {
            byte[] port = new byte[1];
            System.arraycopy(data, CmdConstans.HEAD_CMD.length + 1, port, 0, port.length);
            return port;
        }
        return null;
    }

    private byte[] getFrequency(byte[] data) {
        if (data != null && data.length > CmdConstans.HEAD_CMD.length + 2) {
            byte[] frequency = new byte[2];
            System.arraycopy(data, CmdConstans.HEAD_CMD.length + 2, frequency, 0, frequency.length);
            return frequency;
        }
        return null;
    }


    private byte[] getTime(byte[] data) {
        if (data != null && data.length > CmdConstans.HEAD_CMD.length + 4) {
            byte[] time = new byte[2];
            System.arraycopy(data, CmdConstans.HEAD_CMD.length + 4, time, 0, time.length);
            return time;
        }
        return null;
    }

    private byte[] getCalorie(byte[] data) {
        if (data != null && data.length > CmdConstans.HEAD_CMD.length + 6) {
            byte[] calorie = new byte[2];
            System.arraycopy(data, CmdConstans.HEAD_CMD.length + 6, calorie, 0, calorie.length);
            return calorie;
        }
        return null;
    }

    private byte[] getFrec(byte[] data) {
        if (data != null && data.length > CmdConstans.HEAD_CMD.length + 8) {
            byte[] calorie = new byte[2];
            System.arraycopy(data, CmdConstans.HEAD_CMD.length + 8, calorie, 0, calorie.length);
            return calorie;
        }
        return null;
    }

    private byte getCmdType(byte[] data) {
        if (data != null && data.length > CmdConstans.HEAD_CMD.length + 1) {
            return data[CmdConstans.HEAD_CMD.length];
        }
        return 0;
    }

    /**
     * 检查数据是否有结束符.
     */
    private boolean checkEndCmd(byte[] data) {
        if (data != null && data.length >= CmdConstans.END_CMD.length) {
            return HexDump.bytesEndWith(data, CmdConstans.END_CMD);
        }
        return false;
    }

    /**
     * 检查数据是否有开始符
     */
    private boolean checkStartCmd(byte[] data) {
        if (data != null && data.length >= CmdConstans.HEAD_CMD.length) {
            return HexDump.bytesStartWith(data, CmdConstans.HEAD_CMD);
        }
        return false;
    }

    //移除数据头
    private byte[] removeStartCmd(byte[] data) {
        if (checkStartCmd(data)) {
            int startCmdLength = CmdConstans.HEAD_CMD.length;
            byte[] bs = new byte[data.length - startCmdLength];
            System.arraycopy(data, startCmdLength, bs, 0, bs.length);
            return bs;
        } else {
            return data;
        }
    }

    //去掉头尾，获取中间数据
    private static byte[] getData(byte[] data) {
        int startCmdLength = CmdConstans.HEAD_CMD.length + 1;//包括一个类型命令
        int endCmdLength = CmdConstans.END_CMD.length + 1;//包括一个校验符
        if (data.length < startCmdLength + endCmdLength) {
            return null;
        }
        byte[] bs = new byte[data.length - startCmdLength];
        System.arraycopy(data, startCmdLength, bs, 0, bs.length);
        byte[] bs1 = new byte[bs.length - endCmdLength];
        System.arraycopy(bs, 0, bs1, 0, bs1.length);
        return bs1;
    }

    /**
     * 进行异或校验
     */
    private boolean checkXor(byte[] data) {
        if (data != null && data.length > CmdConstans.END_CMD.length + 1) {
            if (data[data.length - CmdConstans.END_CMD.length - 1] == getXor(data)) {
                return true;
            }
        }
        return false;
    }

    public byte getXor(byte[] datas) {
        byte[] data = getData(datas);
        byte temp = data[0];

        for (int i = 1; i < data.length; i++) {
            temp ^= data[i];
        }
        return temp;
    }

    public interface AnalyseDataListener {
        void notifyMotionData(int port, int frequency, int time, int calorie, int frec);

        void notifyResult(int port, int frequency, int time, int calorie, int frec);

        void notifyEnd();

        void notifyDataErr();
    }
}
