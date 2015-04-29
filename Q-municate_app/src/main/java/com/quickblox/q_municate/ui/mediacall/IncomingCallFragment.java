package com.quickblox.q_municate.ui.mediacall;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quickblox.q_municate.R;
import com.quickblox.q_municate.ui.base.BaseFragment;
import com.quickblox.q_municate.ui.views.RoundedImageView;
import com.quickblox.q_municate.utils.Consts;
import com.quickblox.q_municate_core.models.User;
import com.quickblox.q_municate_core.utils.ConstsCore;
import com.quickblox.q_municate_core.utils.ErrorUtils;
import com.quickblox.videochat.webrtc.QBRTCSessionDescription;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import java.util.ArrayList;

public class IncomingCallFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = IncomingCallFragment.class.getSimpleName();
    private IncomingCallFragmentInterface incomingCallClickListener;
    private User friend;
    private ArrayList<Integer> opponents;
    private boolean isVideoCall;
    private QBRTCTypes.QBConferenceType callType;
    private QBRTCSessionDescription sessionDescription;
//    private User friendFromDB;


    public static IncomingCallFragment newInstance(QBRTCTypes.QBConferenceType callType, User friend) {
        IncomingCallFragment fragment = new IncomingCallFragment();
        Bundle args = new Bundle();
        args.putSerializable(ConstsCore.CALL_TYPE_EXTRA, callType);
        args.putSerializable(ConstsCore.EXTRA_FRIEND, friend);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_popup_call, container, false);

        parseIncomeParameters();
        initUI(rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acceptCallButton:
                accept();
                break;
            case R.id.denyCallButton:
                reject();
                break;
            default:
                break;
        }
    }

    private void reject() {
        if (incomingCallClickListener != null) {
            incomingCallClickListener.rejectCallClick();
        }
    }


    private void accept() {
        if (incomingCallClickListener != null) {
            incomingCallClickListener.acceptCallClick();
        }
    }

    private void parseIncomeParameters(){

        if (getArguments() != null) {
            sessionDescription = (QBRTCSessionDescription) getArguments().getSerializable("sessionDescription");
            friend = (User) getArguments().getSerializable(ConstsCore.EXTRA_FRIEND);
            callType = (QBRTCTypes.QBConferenceType) getArguments().getSerializable(ConstsCore.CALL_TYPE_EXTRA);
        }
    }

    private void initUI(View rootView) {
        isVideoCall = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO.equals(callType);
        ((ImageButton) rootView.findViewById(R.id.acceptCallButton)).setImageResource(
                isVideoCall ? R.drawable.ic_video : R.drawable.ic_call);
        ((TextView) rootView.findViewById(R.id.callTextView)).setText(
                isVideoCall ? R.string.cll_incoming_call_video : R.string.cll_incoming_call_audio);
//        ((TextView) rootView.findViewById(R.id.name_textview)).setText(/*friend*/friendFromDB.getFullName());
        RoundedImageView avatarView = (RoundedImageView) rootView.findViewById(R.id.avatar_imageview);
        avatarView.setOval(true);
//        if(!TextUtils.isEmpty(/*friend*/friendFromDB.getAvatarUrl())){
//            ImageLoader.getInstance().displayImage(/*friend*/friendFromDB.getAvatarUrl(),
//                    avatarView, Consts.UIL_USER_AVATAR_DISPLAY_OPTIONS);
//        }
        rootView.findViewById(R.id.acceptCallButton).setOnClickListener(this);
        rootView.findViewById(R.id.denyCallButton).setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            incomingCallClickListener = (IncomingCallFragmentInterface) activity;
        } catch (ClassCastException e) {
            ErrorUtils.logError(TAG, e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        incomingCallClickListener = null;
    }
}