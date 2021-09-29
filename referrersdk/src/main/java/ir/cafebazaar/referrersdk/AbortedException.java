package ir.cafebazaar.referrersdk;

public class AbortedException extends InterruptedException {
    public AbortedException() {
    }

    public AbortedException(String detailMessage) {
        super(detailMessage);
    }
}
