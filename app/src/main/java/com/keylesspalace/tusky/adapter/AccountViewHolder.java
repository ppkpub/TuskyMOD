package com.keylesspalace.tusky.adapter;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keylesspalace.tusky.BaseActivity;
import com.keylesspalace.tusky.R;
import com.keylesspalace.tusky.entity.Account;
import com.keylesspalace.tusky.interfaces.AccountActionListener;
import com.keylesspalace.tusky.interfaces.LinkListener;
import com.keylesspalace.tusky.util.CustomEmojiHelper;
import com.keylesspalace.tusky.util.ImageLoadingHelper;

import org.json.JSONObject;
import org.ppkpub.ppklib.ODIN;
import org.ppkpub.ppklib.PPkDefine;
import org.ppkpub.ppklib.PTAP01DID;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class AccountViewHolder extends RecyclerView.ViewHolder {
    private TextView username;
    private TextView displayName;
    private ImageView avatar;
    private ImageView avatarInset;
    private String accountId;
    private boolean showBotOverlay;
    private boolean animateAvatar;

    public AccountViewHolder(View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.account_username);
        displayName = itemView.findViewById(R.id.account_display_name);
        avatar = itemView.findViewById(R.id.account_avatar);
        avatarInset = itemView.findViewById(R.id.account_avatar_inset);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
        showBotOverlay = sharedPrefs.getBoolean("showBotOverlay", true);
        animateAvatar = sharedPrefs.getBoolean("animateGifAvatars", false);
    }

    public void setupWithAccount(Account account) {
        accountId = account.getId();

        //modified by ppkpub,20210116
        try{
            this.syncPPkEnd=false;
            this.syncedOdinAvatarURL=null;
            ArrayList result_array = ODIN.matchPPkURIs(account.getName());
            if ( result_array.size()>0 ) {
                final String user_odin_uri =  (String)result_array.get(0);
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        syncUpdateOdinInfo(user_odin_uri,account.getUsername());
                    }
                }).start();
                while (!syncPPkEnd) {
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }


        if(!syncPPkEnd) {
            String format = username.getContext().getString(R.string.status_username_format);
            //pending by ppkpub,20210114
            String formattedUsername = String.format(format, account.getUsername());
            username.setText(formattedUsername);
            CharSequence emojifiedName = CustomEmojiHelper.emojify(account.getName(), account.getEmojis(), displayName);
            displayName.setText(emojifiedName);
        }

        if( syncedOdinAvatarURL == null )
            syncedOdinAvatarURL= account.getAvatar();

        int avatarRadius = avatar.getContext().getResources()
                .getDimensionPixelSize(R.dimen.avatar_radius_48dp);
        ImageLoadingHelper.loadAvatar(syncedOdinAvatarURL, avatar, avatarRadius, animateAvatar);
        //end

        if (showBotOverlay && account.getBot()) {
            avatarInset.setVisibility(View.VISIBLE);
            avatarInset.setImageResource(R.drawable.ic_bot_24dp);
            avatarInset.setBackgroundColor(0x50ffffff);
        } else {
            avatarInset.setVisibility(View.GONE);
        }
    }

    //added by ppkpub,20210116
    private boolean syncPPkEnd=false;
    private String syncedOdinAvatarURL=null;

    private void syncUpdateOdinInfo(String user_odin_uri,String user_sns_name){
        JSONObject user_info = PTAP01DID.getPubUserInfo(user_odin_uri);
        if(user_info!=null) {
            if(PTAP01DID.isSameSnsUser("ActivityPub",user_sns_name,BaseActivity.ACTIVE_HOST_DOMAIN,user_info.optJSONObject(PPkDefine.ODIN_EXT_KEY_SNS))) {
                username.setText("@"+user_odin_uri);
                displayName.setText(user_info.optString("name", "anonymous"));
                String odin_avatar = user_info.optString("avatar");
                if(odin_avatar!=null && odin_avatar.length()>0) {
                    syncedOdinAvatarURL=odin_avatar;
                }
            }else{
                displayName.setText("Wrong ODIN");
                username.setText("@"+user_sns_name);
            }
        }
        syncPPkEnd=true;
    }

    void setupActionListener(final AccountActionListener listener) {
        itemView.setOnClickListener(v -> listener.onViewAccount(accountId));
    }

    public void setupLinkListener(final LinkListener listener) {
        itemView.setOnClickListener(v -> listener.onViewAccount(accountId));
    }
}