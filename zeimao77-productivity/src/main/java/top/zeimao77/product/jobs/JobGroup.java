package top.zeimao77.product.jobs;

import java.util.ArrayList;

public class JobGroup implements IJob{

    private ArrayList<IJob> jobList;
    private String jobId;

    public JobGroup(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public String jobId() {
        return this.jobId;
    }

    public int size() {
        return jobList.size();
    }

    public synchronized boolean addJob(IJob job) {
        if(this.jobList == null) {
            this.jobList = new ArrayList<>();
        }
        return this.jobList.add(job);
    }

    public String getJobId() {
        return jobId;
    }

    public ArrayList<IJob> getJobList() {
        return jobList;
    }

    public void setJobList(ArrayList<IJob> jobList) {
        this.jobList = jobList;
    }
}
