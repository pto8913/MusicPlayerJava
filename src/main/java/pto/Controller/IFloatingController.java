package pto.Controller;

public interface IFloatingController {
    public boolean isOpen();
    public void playOpenAnimation();
    public void playCloseAnimation();
    public boolean isIgnoreAllClose();
    public void setIgnoreAllClose(boolean in);
}
