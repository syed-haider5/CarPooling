package krist.car;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.usage.NetworkStats;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.INotificationSideChannel;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;



import static android.content.ContentValues.TAG;
import static android.support.v4.content.ContextCompat.getCodeCacheDir;
import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by tagolli on 5/31/18.
 */

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> implements Filterable{


    private List<TripsModel> dataSet;
    private List<TripsModel> dataSetFiltered;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View list_item_view;
        public ImageView imageView;
        public TextView vNisja, vMberritja, data, ora, vendet;
        public Button list_button;
        FirebaseUser idauth;
        private FirebaseAuth mAuth;
        private StorageReference mStorageRef;

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference databaseUsers = database.getReference("users");





        public ViewHolder(View v) {

            super(v);

            list_item_view = v;

            imageView = list_item_view.findViewById(R.id.img_mak);
            vNisja = list_item_view.findViewById(R.id.new_nisja);
            vMberritja = list_item_view.findViewById(R.id.new_mberritja);
            data = list_item_view.findViewById(R.id.new_ora);
            ora = list_item_view.findViewById(R.id.new_data);
            vendet = list_item_view.findViewById(R.id.new_vendet);



            idauth = FirebaseAuth.getInstance().getCurrentUser();
            mStorageRef = FirebaseStorage.getInstance().getReference();


        }
    }





    public TripsAdapter(List<TripsModel> dataSet){

        this.dataSet = dataSet;
        dataSetFiltered = new ArrayList<>(dataSet);
    }

    public TripsAdapter(){};



    public void setSearchOperation(List<TripsModel> newList){

      dataSet = new ArrayList<>();
      dataSet.addAll(newList);
      notifyDataSetChanged();


    }



    @NonNull
    @Override
    public TripsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View trips_view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.trips_list_item_2,parent,false);






        return new ViewHolder(trips_view);
    }


    public void deleteItem(int position){

        dataSet.remove(position);
        notifyDataSetChanged();

    }




    @Override
    public void onBindViewHolder(@NonNull final TripsAdapter.ViewHolder holder, final int position) {
        holder.vMberritja.setText(dataSet.get(position).getvMberritja());
        holder.vNisja.setText(dataSet.get(position).getvNisja());
        holder.data.setText(dataSet.get(position).getData());
        holder.ora.setText(dataSet.get(position).getOra());
        holder.vendet.setText(dataSet.get(position).getVendet());
        Picasso.get().load(dataSet.get(position).getUri()).fit().centerCrop().into(holder.imageView);









                //.fit().centerCrop().into(holder.imageView);

        //Picasso.get().load(dataSet.get(position).getIdShofer()).into(holder.imageView);

 /*            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        FirebaseUser idauth = FirebaseAuth.getInstance().getCurrentUser();
        idauth.getUid();
        String id = idauth.getUid();nem
        mStorageRef.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load( dataSet.get(position).getMap()).resize(90,70).centerCrop().into(holder.imageView);

            }
        });
*/




        holder.list_item_view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              // Toast.makeText(v.getContext(), dataSet.get(position).getTripID(), Toast.LENGTH_SHORT).show();
                //Krijo Dialog per tripID kalo te trpi  id e usert qe e zgjedh ///

                FirebaseUser idauth = FirebaseAuth.getInstance().getCurrentUser();

                String id = idauth.getUid();


                if(dataSet.get(position).getVendet().equals("0")){
                    Toast.makeText(v.getContext(), "Udhetim i plotesuar 0 vende te lira", Toast.LENGTH_LONG).show();
                }else if(dataSet.get(position).getIdShofer().equals(id)){
                    Toast.makeText(v.getContext(), "Udhetim i postuar nga ju", Toast.LENGTH_LONG).show();
                }else {


                    String shoferId = dataSet.get(position).getIdShofer();
                    String tripId = dataSet.get(position).getTripID();
                    Intent myIntent = new Intent(v.getContext(), PopUpActivity.class);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) v.getContext(),holder.imageView, ViewCompat.getTransitionName(holder.imageView));







                    Bundle bundle = new Bundle();
                    bundle.putString("shoferID", shoferId);
                    bundle.putString("tripID", tripId);
                    myIntent.putExtra("bS", bundle);


                    Log.v(TAG, "Kalimi id");

                    v.getContext().startActivity(myIntent, optionsCompat.toBundle());


                }


























                // ngjit id e userit te tripi


               /* String tripId = dataSet.get(position).getTripID();

                FirebaseUser idauth;
                idauth = FirebaseAuth.getInstance().getCurrentUser();
                String id = idauth.getUid();

                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference databaseUsers = database.getReference("trips");

                databaseUsers.child(tripId).push().setValue(id);

*/


            }
        });

    }

    public void filterList(ArrayList<TripsModel> filteredList){
        dataSet = filteredList;
        notifyDataSetChanged();
    }




    @Override
    public int getItemCount() {
        return dataSet.size();
    }






    @Override
    public Filter getFilter() {

        return dataFiltered;
    }


    private Filter dataFiltered = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<TripsModel> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(dataSetFiltered);
            }else {
                String filterPatternn = charSequence.toString().toLowerCase().trim();

                for (TripsModel model : dataSetFiltered){
                    if (model.getvNisja().toLowerCase().contains(filterPatternn)){
                        filteredList.add(model);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            dataSet.clear();
            dataSet.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };




}
