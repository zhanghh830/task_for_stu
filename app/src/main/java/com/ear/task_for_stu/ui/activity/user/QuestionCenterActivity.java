package com.ear.task_for_stu.ui.activity.user;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ear.task_for_stu.R;
import com.ear.task_for_stu.config.Config;
import com.ear.task_for_stu.net.CommonCallback;
import com.ear.task_for_stu.ui.activity.BaseActivity;
import com.ear.task_for_stu.ui.vo.Question;
import com.ear.task_for_stu.biz.QuestionBiz;
import com.ear.task_for_stu.listener.ClickListener;
import com.ear.task_for_stu.ui.adpter.QuestionAdapter;
import com.ear.task_for_stu.ui.view.SwipeRefresh;
import com.ear.task_for_stu.ui.view.SwipeRefreshLayout;
import com.ear.task_for_stu.ui.vo.utils.T;
import com.leon.lfilepickerlibrary.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QuestionCenterActivity extends BaseActivity {
    LinearLayout questionCenter;
    LinearLayout TaskCenter;
    LinearLayout UserCenter;
    LinearLayout lead;
    TextView all;
    TextView waitedSolve;
    TextView solved;
    EditText searchedContent;
    ImageView searchBtn;
    SwipeRefreshLayout eSwipeRefreshLayout;
    RecyclerView eRecyclerView;

    QuestionAdapter questionAdapter;
    List<Question> questionList;
    QuestionBiz questionBiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_center);
        initView();
        initEvent();
        setTitle("问答中心");
    }

    private void initEvent() {
        setToolbar(R.drawable.common_add, new ClickListener() {
            @Override
            public void click() {
                toCreateNewQuestionActivity();
            }
        });
        lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPhotoctivity();
            }
        });
        questionCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toQuestionCenterActivity();
            }
        });

        TaskCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toImageUploadActivity();
            }
        });

        UserCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toUserCenterActivity();
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 展示全部状态（除未审核的信息）
                loadAll();
            }
        });

        waitedSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 展示待解决的问题
                startLoadingProgress();
                questionBiz.searchByState(Config.QUESTION_WAIT_SOLVE, new CommonCallback<List<Question>>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(List<Question> response) {
                        stopLoadingProgress();
                        T.showToast("查询成功！");
                        updateList(response);
                    }
                });
            }
        });

        solved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 展示已解决的问题
                startLoadingProgress();
                questionBiz.searchByState(Config.USER_PASSED, new CommonCallback<List<Question>>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(List<Question> response) {
                        stopLoadingProgress();
                        T.showToast("查询成功！");
                        updateList(response);
                    }
                });
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 从服务端搜索
                String s = searchedContent.getText().toString();
                if(!StringUtils.isEmpty(s)){
                    T.showToast("查询的标题不能为空哦~");
                }
                startLoadingProgress();
                questionBiz.searchByTitle(s, new CommonCallback<List<Question>>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(List<Question> response) {
                        stopLoadingProgress();
                        T.showToast("查询成功！");
                        updateList(response);
                    }
                });
            }
        });

        eSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAll();
            }
        });

        eSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadAll();
            }
        });
    }

    private void loadAll() {
        //TODO 查询所有问题
        startLoadingProgress();
        questionBiz.getAll(new CommonCallback<List<Question>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                eSwipeRefreshLayout.setRefreshing(false);
                eSwipeRefreshLayout.setPullUpRefreshing(false);
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(List<Question> response) {
                stopLoadingProgress();
                T.showToast("更新问答数据成功！");
                updateList(response);
            }
        });
    }

    private void updateList(List<Question> response) {
        questionList.clear();
        questionList.addAll(response);
        questionAdapter.notifyDataSetChanged();
        eSwipeRefreshLayout.setRefreshing(false);
        eSwipeRefreshLayout.setPullUpRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadAll();
    }

    private void initView() {
        questionCenter = findViewById(R.id.id_ll_questionCenter);
        TaskCenter = findViewById(R.id.id_ll_taskCenter);
        UserCenter = findViewById(R.id.id_ll_userCenter);
        lead = findViewById(R.id.id_ll_leaderBoard);
        all = findViewById(R.id.id_tv_all);
        waitedSolve = findViewById(R.id.id_tv_waitedSolve);
        solved = findViewById(R.id.id_tv_solved);
        searchedContent = findViewById(R.id.id_et_search);
        searchBtn = findViewById(R.id.id_iv_search);
        eSwipeRefreshLayout = findViewById(R.id.id_swiperefresh);
        eRecyclerView = findViewById(R.id.id_recyclerview);

        eSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        eSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLACK,Color.YELLOW,Color.GREEN);
        questionBiz = new QuestionBiz();
        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(this,questionList);
        eRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eRecyclerView.setAdapter(questionAdapter);
        loadAll();
    }

    private void toCreateNewQuestionActivity() {
        Intent intent = new Intent(this,CreateNewQuestionActivity.class);
        startActivityForResult(intent,1001);
    }

    private void toPhotoctivity() {
        Intent intent = new Intent(this,PhotoActivity.class);
        startActivity(intent);
    }

    private void toUserCenterActivity() {
        Intent intent = new Intent(this,BasicInfoActivity.class);
        startActivity(intent);
        finish();
    }

    private void toImageUploadActivity() {
        Intent intent = new Intent(this, ImageUploadActivity.class);
        startActivity(intent);
        finish();
    }

    private void toQuestionCenterActivity() {
        Intent intent = new Intent(this,QuestionCenterActivity.class);
        startActivity(intent);
        finish();
    }

}