package ir.cafebazaar.referrersdk.communicators;

public class AbortedException extends InterruptedException {
    public AbortedException() {
    }

    public AbortedException(String detailMessage) {
        super(detailMessage);
    }
}
