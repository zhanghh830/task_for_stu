package com.ear.task_for_stu.ui.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ear.task_for_stu.R;
import com.ear.task_for_stu.UserInfoHolder;
import com.ear.task_for_stu.bean.BaseTask;
import com.ear.task_for_stu.bean.User;
import com.ear.task_for_stu.biz.QuestionBiz;
import com.ear.task_for_stu.config.Config;
import com.ear.task_for_stu.net.CommonCallback;
import com.ear.task_for_stu.ui.activity.BaseActivity;
import com.ear.task_for_stu.ui.vo.Question;
import com.ear.task_for_stu.ui.vo.utils.BasicUtils;
import com.ear.task_for_stu.ui.vo.utils.T;
import com.zhy.http.okhttp.utils.L;

public class CreateNewQuestionActivity extends BaseActivity {
    EditText title;
    EditText content;
    EditText reward;
    Button button;
    Question question;
    QuestionBiz questionBiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_question);
        setUpToolBar();
        setTitle("创建新问题");
        initView();
        initEvent();
    }

    private void initEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String te = title.getText().toString();
                String ct = content.getText().toString();
                if (!BasicUtils.StringArrayEmpty(new String[]{te, ct })) {
                    T.showToast("输入的信息不能为空哦~");
                    return;
                }


                User user = UserInfoHolder.getInstance().geteUser();


                UserInfoHolder.getInstance().setUser(user);
                BaseTask baseTask = new BaseTask();
                baseTask.setContentDesc(ct);
                baseTask.setPuid(user.getId());
                baseTask.setTitle(te);
                baseTask.setJugFlag((byte) Config.IS_QUESTION);
                //TODO 管理员功能实现后应订正为待审核
                baseTask.setCurrentState(Config.WAIT_PASSED);
//                baseTask.setPostDate(new Date());
                baseTask.setStateMsg("暂无新消息");
                L.e(baseTask.toString());
                question.setUser(user);
                question.setContext(baseTask);
                startLoadingProgress();
                questionBiz.save(question, new CommonCallback<String>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                        finish();
                    }

                    @Override
                    public void onSuccess(String response) {
                        stopLoadingProgress();
                        T.showToast(response);
                        finish();
                    }
                });
            }
        });
    }

    private void initView() {
        title = findViewById(R.id.id_et_title);
        content = findViewById(R.id.id_et_content);

        button = findViewById(R.id.id_btn_submit);
        question = new Question();
        questionBiz = new QuestionBiz();
    }
}
