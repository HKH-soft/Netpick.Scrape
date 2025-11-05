package ir.netpick.mailmine.common.enums;

public enum PipelineStateEnum {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED;
    // CANCELLED, RETRYING, PAUSED;

    public boolean isFinished() {
        return this == COMPLETED || this == FAILED;
    }
}
