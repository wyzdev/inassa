package com.inassa.inassa.interfaces;

/**
 * Created by root on 2/21/17.
 */

public interface Communicator {
    public void toAuthBySwipe();
    public void toAuthByNumber();
    public void toInformationClient();
    public void toAuthentification();
    public void back();
    public void resetReader();
    public void sleepReader();
    public void setTimoutReader(int time);
    public void getAudioJackStatus();
}
