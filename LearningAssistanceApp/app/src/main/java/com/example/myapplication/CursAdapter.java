package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Models.Course;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CursAdapter extends BaseAdapter implements ListAdapter {
    final private Context context;
    AlertDialog.Builder builder;
    ArrayList<Course> courseList;
    String materie;
    StorageReference storageReference;

    public CursAdapter(ArrayList<Course> courseList, Context context,String materie) {
        this.courseList = courseList;
        this.context = context;
        this.materie = materie;
    }
    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(view == null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.lv_item,null);
        }

        TextView titluCurs= view.findViewById(R.id.titluCurs);
        titluCurs.setText(courseList.get(position).getTitlu());

        ImageView downloadBTN= view.findViewById(R.id.downloadBTN);
        downloadBTN.setOnClickListener(v -> {
            builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Vrei sa downloadezi " + courseList.get(position).getTitlu())
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        Toast.makeText(v.getContext(),"ai ales sa downloadezi fisierul",
                                Toast.LENGTH_SHORT).show();
                        downloadPDF(v.getContext(), courseList.get(position).getTitlu(),".pdf", Environment.DIRECTORY_DOWNLOADS,courseList.get(position).getPdfURL());
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Toast.makeText(v.getContext(),"ai ales sa nu downloadezi fisierul",
                                Toast.LENGTH_SHORT).show();
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Vrei sa downloadezi acest fisier ?");
            alert.show();
        });
        return view;
    }



    private long downloadPDF(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
        if (this.materie.equals("OOP")){
            storageReference = FirebaseStorage.getInstance().getReference("courses");}
        if (this.materie.equals("BD")) {
            storageReference = FirebaseStorage.getInstance().getReference("coursesBD");
        }

        DownloadManager.Query query;
        Cursor c;
        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        query = new DownloadManager.Query();
        if(query!=null) {
            query.setFilterByStatus(DownloadManager.STATUS_FAILED|DownloadManager.STATUS_PAUSED|DownloadManager.STATUS_SUCCESSFUL|
                    DownloadManager.STATUS_RUNNING|DownloadManager.STATUS_PENDING);
        }
        c = downloadmanager.query(query);
        if(c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch(status) {
                case DownloadManager.STATUS_PAUSED:
                    Toast.makeText(builder.getContext(), "Download paused!",Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_PENDING:
                    Toast.makeText(builder.getContext(), "Pending!",Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_RUNNING:
                    Toast.makeText(builder.getContext(), "Download Running!",Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Toast.makeText(builder.getContext(), "Download successful",Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(builder.getContext(), "FAILED!",Toast.LENGTH_LONG).show();
                    break;
            }}
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        request.setDescription("File is downloading ...");

        return downloadmanager.enqueue(request);
    }

}
