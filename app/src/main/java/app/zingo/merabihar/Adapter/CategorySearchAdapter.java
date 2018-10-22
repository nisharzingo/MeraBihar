package app.zingo.merabihar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ZingoHotels Tech on 22-10-2018.
 */

public class CategorySearchAdapter extends RecyclerView.Adapter<CategorySearchAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Category> list;
    public CategorySearchAdapter(Context context,ArrayList<Category> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_search_adapter, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Category profile = list.get(position);

        if(profile!=null){

            holder.mName.setText(""+profile.getCategoriesName());
            String base=profile.getCategoriesImage();
            if(base != null && !base.isEmpty()){
                Picasso.with(context).load(base).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(holder.mPhoto);

            }else{
                holder.mPhoto.setImageResource(R.drawable.no_image);
            }

            holder.mProfileLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });



        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        TextView mName;
        LinearLayout mProfileLay;
        CircleImageView mPhoto;
//

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mName = (TextView)itemView.findViewById(R.id.people_name);
            mProfileLay = (LinearLayout) itemView.findViewById(R.id.profile_search_layout);
            mPhoto = (CircleImageView)itemView.findViewById(R.id.people_profile_photo);


        }


    }


}
