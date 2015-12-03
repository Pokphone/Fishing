package com.jude.fishing.module.gofishing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.data.BeamDataActivity;
import com.jude.exgridview.ExGridView;
import com.jude.fishing.R;
import com.jude.fishing.model.AccountModel;
import com.jude.fishing.model.entities.FishingSeed;
import com.jude.fishing.model.entities.PersonBrief;
import com.jude.fishing.module.user.UserDetailActivity;
import com.jude.fishing.utils.RecentDateFormat;
import com.jude.tagview.TAGView;
import com.jude.utils.JTimeTransform;
import com.jude.utils.JUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by heqiang on 2015/12/2.
 */
@RequiresPresenter(FishingDetailPresenter.class)
public class FishingDetailActivity extends BeamDataActivity<FishingDetailPresenter,FishingSeed>{
    @InjectView(R.id.avatar)
    SimpleDraweeView avatar;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.time)
    TextView time;
    @InjectView(R.id.content)
    TextView content;
    @InjectView(R.id.tg_join)
    TAGView join;
    @InjectView(R.id.image)
    ExGridView joinMember;
    @InjectView(R.id.tv_title)
    TextView title;
    boolean joined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fishing_activity_detail);
        ButterKnife.inject(this);
        join.setOnClickListener(v -> getPresenter().joinDate(strTitle,joined));
    }

    String strTitle;
    @Override
    public void setError(Throwable e) {
        getExpansion().showErrorPage();
    }

    @Override
    public void setData(FishingSeed data) {
        getExpansion().dismissProgressPage();
        avatar.setImageURI(Uri.parse(data.getAuthorAvatar()));
        name.setText(data.getAuthorName());
        title.setText(data.getTitle());
        strTitle = data.getTitle();
        time.setText(new JTimeTransform(data.getTime()).toString(new RecentDateFormat()));
        content.setText(data.getContent());
        int uid = AccountModel.getInstance().getAccount().getUID();
        for (PersonBrief personBrief : data.getEnrollMember()) {
            if (personBrief.getUID()== uid){
                joined = true;
                join.setText("进入");
            }
            SimpleDraweeView draweeView = new SimpleDraweeView(FishingDetailActivity.this);
            draweeView.setLayoutParams(new ViewGroup.LayoutParams(JUtils.dip2px(40), JUtils.dip2px(40)));
            draweeView.setImageURI(Uri.parse(personBrief.getAvatar()));
            draweeView.getHierarchy().setRoundingParams(RoundingParams.asCircle());
            draweeView.setOnClickListener(v -> {
                Intent i = new Intent(FishingDetailActivity.this, UserDetailActivity.class);
                i.putExtra("id", personBrief.getUID());
                startActivity(i);
            });
            joinMember.addView(draweeView);
        }
    }
}
