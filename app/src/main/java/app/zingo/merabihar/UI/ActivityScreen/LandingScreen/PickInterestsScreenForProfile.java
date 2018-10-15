package app.zingo.merabihar.UI.ActivityScreen.LandingScreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.merabihar.Adapter.InterestAdapter;
import app.zingo.merabihar.CustomViews.CustomGridView;
import app.zingo.merabihar.Model.Interest;
import app.zingo.merabihar.Model.InterestProfileMapping;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.Events.AboutCity;
import app.zingo.merabihar.UI.ActivityScreen.Events.ListOfEventsActivity;
import app.zingo.merabihar.Util.PreferenceHandler;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.InterestAPI;
import app.zingo.merabihar.WebApi.InterestProfileAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickInterestsScreenForProfile extends AppCompatActivity {
    ImageView mCloseActivityImg;
    CustomGridView customGridView;
    Button mInterest;

    InterestAdapter adapter;

    ArrayList<Integer> zingoInterestId;
    ArrayList<Interest> zingoInterests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_pick_interests_screen_for_profile);

            mCloseActivityImg = (ImageView) findViewById(R.id.tv_header_title);
            setTitle("Interest");

            customGridView = (CustomGridView) findViewById(R.id.interest_grid_view);
            mInterest = (Button) findViewById(R.id.interest_continue_btn);

            mCloseActivityImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(PickInterestsScreenForProfile.this, HomeLandingBottomScreen.class);
                    startActivity(i);
                    PickInterestsScreenForProfile.this.finish();

                }
            });
            getInterests();

            customGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                }
            });

            mInterest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = adapter.getCount();
                    String selected = "";

                    zingoInterestId = new ArrayList<>();
                    for (int j=0;j<i;j++)
                    {
                        //System.out.println();
                        if(((LinearLayout)customGridView.getChildAt(j)).isActivated())
                        {

                            zingoInterestId.add(zingoInterests.get(j).getZingoInterestId());
                        }
                    }

                    if(zingoInterestId!=null&&zingoInterestId.size()!=0)
                    {
                        System.out.println(" Interst ids "+zingoInterestId.size());

                        int count = 0;

                        for (int k =0;k<zingoInterestId.size();k++){
                            count = count+1;
                            InterestProfileMapping pm = new InterestProfileMapping();
                            pm.setZingoInterestId(zingoInterestId.get(k));
                            pm.setProfileId(PreferenceHandler.getInstance(PickInterestsScreenForProfile.this).getUserId());

                            if(count==zingoInterestId.size()){

                                profileInterest(pm,"Completed");

                            }else{

                                profileInterest(pm,"Processing");

                            }

                        }

                    }
                }
            });



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getInterests() {
        final ProgressDialog dialog = new ProgressDialog(PickInterestsScreenForProfile.this);
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                //System.out.println(TAG+" thread started");
                final InterestAPI interestApi = Util.getClient().create(InterestAPI.class);
                Call<ArrayList<Interest>> getCat = interestApi.getInterest();

                getCat.enqueue(new Callback<ArrayList<Interest>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<Interest>> call, Response<ArrayList<Interest>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        int responsecode = response.code();

                        if(responsecode == 200)
                        {
                            //System.out.println(response.body().size());


                            if(response.body() != null && response.body().size() != 0)
                            {
                                zingoInterests = response.body();
                                adapter = new InterestAdapter(PickInterestsScreenForProfile.this,response.body());
                                customGridView.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Interest>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        //System.out.println(TAG+" thread inside on fail");
                        Toast.makeText(PickInterestsScreenForProfile.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    private void profileInterest(final InterestProfileMapping intrst,final String status) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                InterestProfileAPI mapApi = Util.getClient().create(InterestProfileAPI.class);
                Call<InterestProfileMapping> response = mapApi.postInterest(intrst);
                response.enqueue(new Callback<InterestProfileMapping>() {
                    @Override
                    public void onResponse(Call<InterestProfileMapping> call, Response<InterestProfileMapping> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {


                            if(status.equalsIgnoreCase("Completed")){

                                Intent i = new Intent(PickInterestsScreenForProfile.this, HomeLandingBottomScreen.class);
                                startActivity(i);
                                finish();
                            }
                        }
                        else
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<InterestProfileMapping> call, Throwable t) {

                        Toast.makeText(PickInterestsScreenForProfile.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                PickInterestsScreenForProfile.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
