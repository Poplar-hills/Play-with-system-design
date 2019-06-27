package main.java.CallCenter;

import java.util.*;

public class CallHandler {
    private Map<Integer, Respondent> respondentMap;   // todo: 这里类型用接口？还是用具体类？
    private Map<Integer, Manager> managerMap;
    private Map<Integer, Director> directorMap;

    private Queue<Call> respondentCallQueue = new LinkedList<>();
    private Queue<Call> managerCallQueue = new LinkedList<>();
    private Queue<Call> directorCallQueue = new LinkedList<>();

    private Queue<Respondent> idleRespondents = new LinkedList<>();
    private Queue<Respondent> idleManagers = new LinkedList<>();
    private Queue<Respondent> idleDirectors = new LinkedList<>();

    public CallHandler() {
        respondentMap = new
    }

    public void addRespondent(int emplyeeId, int managerId) {
        respondentMap.put(emplyeeId, new Respondent(emplyeeId, managerId));
    }

    public void addManger() {

    }

    public void addDirector() {

    }

    public void receiveCall(int callNumber) {  // todo: 主线程（生产者线程） receive call
        UUID id = UUID.randomUUID();
        Call newCall = new Call(id.toString(), callNumber);
        respondentCallQueue.offer(newCall);
    }

    public void dispatchCall() {  // todo: 新启一个消费者线程来 dispatch call，使用 BlockingQueue
        while (true) {            // 该线程不断循环检查是否有没被 handle 的 call 和空闲的 respondent
            Respondent respondent = idleRespondents.poll();
            Call call = respondentCallQueue.poll();
            if (respondent != null && call != null)
                respondent.answerCall(call);
        }
    }

    public void escalate(Call call, Employee employee) {

    }
}
