package org.ivy;

import org.apache.http.client.methods.HttpGet;
import org.ivy.xutil.http.HttpClientUtil;
import org.junit.Before;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.*;

/**
 * @author Ares miao.xl
 * @version V1.0
 * HttpClientUtilTest
 * 并发测试HttpClientUtil工具性能
 * @date 2017年4月24日 下午11:10:20
 */
public class HttpClientUtilTest {
    // 并发数
    private int count;
    // 测试时长
    private int minutes;

    @Before
    public void setUp() {
        this.count = 10;
        this.minutes = 1;
    }

    /**
     * 循环执行一个任务，直到指定的时间后结束，每个任务包含指定并发数的子任务。
     */
//	@Test
    public void testHttpClient() throws InterruptedException, ExecutionException, TimeoutException {
        // 当前正在执行任务序号
        int currentTaskNo = 0;
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minutes);
        while (System.currentTimeMillis() <= cal.getTimeInMillis()) {
//			Thread.sleep(100);
            Future<String> future = pool.submit(new TaskItem(count, ++currentTaskNo));
            System.out.println(future.get(120, TimeUnit.SECONDS));
        }
        pool.shutdown();
        System.out.println("------------> main thread task finished....");
    }
}

/**
 * @author Ares miao.xl@live.cn
 * @version V1.0
 * TaskItem
 * 单个可执行任务线程 Callable 执行指定并发数的任务，所有任务结束前，该线程保持存活状态
 * @date 2017年4月24日 下午9:55:58
 */
class TaskItem implements Callable<String> {
    // 任务开始计数器
    private final CountDownLatch beginLatch;
    // 任务结束计数器
    private final CountDownLatch finishLatch;
    // 任务并发数
    private int count;
    // 当前线程ID
    private int id;


    public TaskItem(int count, int id) {
        this.count = count;
        this.id = id;
        this.beginLatch = new CountDownLatch(1);
        this.finishLatch = new CountDownLatch(count);
    }

    @Override
    public String call() throws Exception {
        this.launch();
        return "``````````````` No." + id + " task finished........\n";
    }

    private void launch() {

        for (int i = 1; i <= count; i++)
            new Thread(new ApacheCommonsHttpClientPost(beginLatch, finishLatch, i)).start();

        try {
            // 下达任务执行命令，所有任务线程并行执行任务
            this.beginLatch.countDown();
            // 在当前线程执行  await()，起到阻塞作用，避免任务未执行完，当前线程结束
            finishLatch.await();
            // System.out.println("`````````` latch : " + beginLatch.getCount());
            // System.out.println("`````````` finishLatch : " + finishLatch.getCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ApacheCommonsHttpClientPost implements Runnable {
    private static HttpGet get = new HttpGet("http://192.168.15.116:8085/");
    private CountDownLatch beginLatch;
    private CountDownLatch finishLatch;
    private int id;

    public ApacheCommonsHttpClientPost(CountDownLatch beginLatch, CountDownLatch finishLatch, int id) {
        this.beginLatch = beginLatch;
        this.finishLatch = finishLatch;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            // 执行等待，用于等待其他并行线程初始化完毕。
            this.beginLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.launch();
        this.finishLatch.countDown();
    }

    private void launch() {
        // String resp = HttpClientUtils.postSubmit("http://192.168.15.43:8088/", id + "");
        // String resp = new String(HttpClient.service("http://10.120.58.45:8080/hello/", "POST", null));

        try {
            HttpClientUtil.buildHttpClient().execute(get, HttpClientUtil.context);
            // System.out.println(EntityUtils.toString(response.getEntity(), Consts.UTF_8));
            System.out.println("========> " + this.id + ", " + "done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}









