package com.saiyi.gymequipment.me.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.activity.ScanByQRActivity;
import com.saiyi.gymequipment.common.constans.Constant;
import com.saiyi.gymequipment.me.adapter.CustomerQuestionsAdapter;
import com.saiyi.gymequipment.me.model.bean.ExerciseVolume;
import com.saiyi.gymequipment.me.model.bean.Question;
import com.saiyi.gymequipment.me.presenter.EquipmentRepairPresenter;
import com.saiyi.libfast.helper.ImgSetHelper;
import com.saiyi.libfast.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EquipmentRepairActivity extends BKMVPActivity<EquipmentRepairPresenter> {

    private static final int ET_QUESTION_MAX_LENGTH = 100;

    @BindView(R.id.gv_questions)
    GridView gvQuestions;
    @BindView(R.id.bt_submit)
    TextView btSubmit;
    @BindView(R.id.question_et)
    EditText questionEt;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_question_input)
    TextView tvQuestionInput;

    private List<Question> datas = new ArrayList<>();
    private CustomerQuestionsAdapter adapter;
    private boolean chiledQuestion = false;
    private ImgSetHelper mImgSetHelper;
    private File imgFile;
    private String mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_repair);
    }

    @Override
    protected void initView() {
        getTitleBar().setTitle(getString(R.string.equipment_repair));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mac = bundle.getString(Constant.BUNDLE_KEY_DATA);
        }
        getPresenter().getQuestions();
        adapter = new CustomerQuestionsAdapter(datas, this);
        gvQuestions.setAdapter(adapter);
        gvQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setClickItme(i);
                chiledQuestion = true;
                if (!TextUtils.isEmpty(questionEt.getText().toString().trim())) {
                    btSubmit.setEnabled(true);
                }
            }
        });
        tvQuestionInput.setText(String.format(getString(R.string.question_input_length), ET_QUESTION_MAX_LENGTH - questionEt.length() + ""));
        questionEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (chiledQuestion && !TextUtils.isEmpty(s)) {
                    btSubmit.setEnabled(true);
                } else {
                    btSubmit.setEnabled(false);
                }
                tvQuestionInput.setText(String.format(getString(R.string.question_input_length), ET_QUESTION_MAX_LENGTH - s.length() + ""));
            }
        });
        mImgSetHelper = new ImgSetHelper(this, FileUtils.getAppFileDir(this).getPath() + "/customer_img.jpg");
        mImgSetHelper.setListener(setListener);
    }

    protected ImgSetHelper.SetListener setListener = new ImgSetHelper.SetListener() {
        @Override
        public void onCropSuccess(File file) {
            imgFile = file;
            Picasso.with(mContext).load(file).placeholder(R.drawable.picture).error(R.drawable.picture).into(ivImg);
        }

        @Override
        public void onCropFaild() {
        }
    };

    @Override
    public EquipmentRepairPresenter initPresenter(Context context) {
        return new EquipmentRepairPresenter(context);
    }

    public void showCustomLoading() {
        showCustomLoading(getString(R.string.get_dataing));
    }

    public void showCustomsubmiting() {
        showCustomLoading(getString(R.string.submiting));
    }

    public void addFeedbackSuccess() {
        dismissProgressDialog();
        toast(R.string.submit_success);
        back();
    }

    public void addFeedbackFaild(String msg) {
        dismissProgressDialog();
        toast(msg);
    }

    public void getQuestionsSuccess(List<Question> list) {
        dismissProgressDialog();
        this.datas = list;
        adapter.updateData(list);
    }

    public void getQuestionsFaild(String msg) {
        dismissProgressDialog();
    }

    @OnClick({R.id.bt_submit, R.id.iv_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                send();
                break;
            case R.id.iv_img:
                mImgSetHelper.takeAPhoto();
                break;
        }
    }


    private void send() {
        if (TextUtils.isEmpty(mac)) {
            toast(R.string.please_scan);
            return;
        }
        Question question = adapter.getQuestion();
        if (question == null) {
            toast(R.string.please_resultquestion);
            return;
        }
        String qmsg = questionEt.getText().toString().trim();
        if (TextUtils.isEmpty(qmsg)) {
            toast(R.string.please_input_msg);
            return;
        }
        getPresenter().addFeedback(mac, qmsg, question.getIdQuestion(), imgFile);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mImgSetHelper.onActivityResult(requestCode, resultCode, data);
    }
}
