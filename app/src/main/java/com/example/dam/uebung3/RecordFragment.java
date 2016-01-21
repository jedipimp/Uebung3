package com.example.dam.uebung3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dam.uebung3.Model.Record;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment {
    private Record record;
    private File file;

    private OnFragmentInteractionListener mListener;
    //private MainActivity mainActivity;



    public RecordFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment RecordFragment.
     */
    public static RecordFragment newInstance(Record record, File file) {
        RecordFragment fragment = new RecordFragment();
        fragment.record = record;
        fragment.file = file;
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        // set record id field
        final TextView recordId = (TextView) view.findViewById(R.id.recordIdTextView);
        recordId.setText("id: " + record.getId());

        TextView mlsLngTextView = (TextView) view.findViewById(R.id.mlsLngTextView);
        mlsLngTextView.setText(record.getMlsLng()+"");


        TextView mlsLatTextView = (TextView) view.findViewById(R.id.mlsLatTextView);
        mlsLatTextView.setText(record.getMlsLat()+"");

        TextView gpsLngTextView = (TextView) view.findViewById(R.id.gpsLngTextView);
        gpsLngTextView.setText(record.getGpsLng()+"");


        TextView gpsLatTextView = (TextView) view.findViewById(R.id.gpsLatTextView);
        gpsLatTextView.setText(record.getGpsLat()+"");

        TextView gpsAccTextView = (TextView) view.findViewById(R.id.gpsAccuracyTextView);
        gpsAccTextView.setText(record.getGpsAcc() + " m");

        TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        dateTextView.setText(record.getDate()+"");

        TextView distanceTextView = (TextView) view.findViewById(R.id.distanceTextView);
        distanceTextView.setText(record.getDistance() + "");

        Button deleteButton = (Button) view.findViewById(R.id.deleteRecordButtonId);
        deleteButton.setOnClickListener(new DeleteOnClickListener(this));


        Button sendEmailButton = (Button) view.findViewById(R.id.sendEmailRecordButtonId);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "MASE Übung 3");
                intent.putExtra(Intent.EXTRA_TEXT, record.toString());

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public Record getRecord()
    {
        return record;
    }

    public File getFile() {return file;}
}
