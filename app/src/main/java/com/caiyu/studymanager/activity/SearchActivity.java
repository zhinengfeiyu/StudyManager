package com.caiyu.studymanager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.caiyu.entity.ClassTableEntity;
import com.caiyu.studymanager.R;
import com.caiyu.studymanager.common.Verifier;
import com.caiyu.studymanager.constant.ExtraKeys;
import com.caiyu.studymanager.constant.Server;
import com.caiyu.studymanager.manager.ClassTableManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by 渝 on 2016/3/7.
 */
public class SearchActivity extends BaseActivity {

    @Bind(R.id.inputEt)
    EditText inputEt;
    @Bind(R.id.confirmBtn)
    Button confirmBtn;
    @Bind(R.id.choiceLv)
    ListView choiceLv;

    private ClassTableManager classManager = ClassTableManager.getInstance();

    public static final int TYPE_CLASS = 1;
    public static final int TYPE_CLASS_ROOM = 2;
    public static final int TYPE_TEACHER = 3;

    private int inputType;
    private long classId;

    @Override
    public int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    public void afterViewCreated() {
        inputType = getIntent().getIntExtra(ExtraKeys.SEARCH_TYPE, 1);
        String titleStr;
        switch (inputType) {
            case TYPE_CLASS_ROOM:
                titleStr = "设置教室";
                break;
            case TYPE_TEACHER:
                titleStr = "设置教师";
                break;
            default:
                titleStr = "设置";
        }
        setTitle(titleStr);
        if (inputType == TYPE_TEACHER) {
            confirmBtn.setVisibility(View.GONE);
        }
        classId = getIntent().getLongExtra(ExtraKeys.CLASS_TABLE_ENTITY_ID, 1L);
        listResult("");
        initInputChange();
    }

    @Override
    public boolean showBack() {
        return false;
    }

    @OnClick(R.id.cancelBtn)
    void click_cancel() {
        finish();
    }

    @OnClick(R.id.confirmBtn)
    void click_confirm() {
        String inputStr = inputEt.getText().toString().trim();
        saveResult(inputStr);
        returnResult(inputStr);
    }

    @OnItemClick(R.id.choiceLv)
    void choose_item(int position) {
        String inputStr = ((ArrayAdapter<SpannableString>) choiceLv.getAdapter())
                        .getItem(position).toString();
        saveResult(inputStr);
        returnResult(inputStr);
    }

    private void initInputChange() {
        inputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputStr = s.toString().trim();
                confirmBtn.setEnabled(Verifier.isEffectiveStr(inputStr));
                listResult(inputStr);
            }
        });
    }

    private void listResult(final String inputStr) {
        StringRequest request = new StringRequest(Request.Method.POST, Server.SEARCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!Verifier.isEffectiveStr(response)) {
                            choiceLv.setAdapter(new ArrayAdapter<>(
                                    SearchActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    new ArrayList<String>()));
                        }
                        else {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                List<SpannableString> data = new ArrayList<>(jsonArray.length());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String rawText = "";
                                    if (inputType == TYPE_CLASS_ROOM)
                                        rawText = jsonArray.getString(i);
                                    else if (inputType == TYPE_CLASS) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        rawText = jsonObject.getString(Server.RES_SUBJECT_NAME);
                                    }
                                    int firstIndex = rawText.indexOf(inputStr);
                                    int lastIndex = firstIndex + inputStr.length();
                                    SpannableString ss = new SpannableString(rawText);
                                    ss.setSpan(new ForegroundColorSpan(Color.BLUE),
                                            firstIndex,
                                            lastIndex,
                                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                    data.add(ss);
                                }
                                choiceLv.setAdapter(new ArrayAdapter<>(SearchActivity.this,
                                        android.R.layout.simple_list_item_1, data));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "网络错误");
                    }
                }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put(Server.REQ_SEARCH_TYPE, inputType + "");
                map.put(Server.REQ_INPUT, inputStr);
                return map;
            }
        };
        request.setTag("search");
        MyApplication.getRequestQueue().add(request);
    }

    private void saveResult(String inputStr) {
        if (inputType == TYPE_CLASS) {
            ClassTableEntity classEntity = classManager.getDataById(classId);
            classEntity.setClassName(inputStr);
            classManager.update(classEntity);
        }
        else if (inputType == TYPE_CLASS_ROOM) {
            ClassTableEntity classEntity = classManager.getDataById(classId);
            classEntity.setClassRoom(inputStr);
            classManager.update(classEntity);
        }
        else if (inputType == TYPE_TEACHER) {

        }
    }

    private void returnResult(String str) {
        hideSoftInput(inputEt);
        Intent intent = new Intent();
        intent.putExtra(ExtraKeys.UPDATE_RESULT, str);
        setResult(RESULT_OK, intent);
        finish();
    }
}
