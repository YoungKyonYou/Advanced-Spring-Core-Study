package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class FieldLogTrace implements LogTrace{

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private TraceId traceIdHolder; //traceId 동기화 (기존에는 파라미터로 넘겼다면(V2) 이건 여기서 보관한다)

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId=traceIdHolder;
        Long startTimeMs = System.currentTimeMillis();
        //로그 출력
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()),message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    private void syncTraceId(){
        if(traceIdHolder==null){
            traceIdHolder = new TraceId();
        }else{
            traceIdHolder=traceIdHolder.createNextId();
        }
    }

    private void releaseTraceId() {
        if(traceIdHolder.isFirstLevel()){
            traceIdHolder=null; //destroy
        }else{
            traceIdHolder=traceIdHolder.createPreviousId();
        }
    }


    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e){
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if(e == null){
            //예외가 없으면
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()),status.getMessage(), resultTimeMs);

        }else{
            //예외가 있다면
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()),status.getMessage(), resultTimeMs, e.toString());
        }
    }
    private static String addSpace(String prefix, int level){
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i==level-1)?"|"+prefix:"|  ");
        }
        return sb.toString();
    }
}
