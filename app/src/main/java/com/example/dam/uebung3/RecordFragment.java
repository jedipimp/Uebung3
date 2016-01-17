package com.example.dam.uebung3;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dam.uebung3.Model.Record;


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

    private OnFragmentInteractionListener mListener;

    public RecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment RecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance(double mlsLat, double mlsLng, double gpsLat, double gpsLng) {
        RecordFragment fragment = new RecordFragment();
        fragment.record = new Record(mlsLat,mlsLng,gpsLat,gpsLng);

        return fragment;
    }


    public void onViewCreated()
    {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView mlsLngTextView = (TextView) getView().findViewById(R.id.mlsLngTextView);
                Log.d("hello", "hello");
                mlsLngTextView.setText(record.mlsLng + "HASLD");
                TextView mlsLatTextView = (TextView) getView().findViewById(R.id.mlsLatTextView);
                mlsLngTextView.setText(record.mlsLat + " SOME");
            }
        });



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
        TextView mlsLngTextView = (TextView)v.findViewById(R.id.mlsLngTextView);
        mlsLngTextView.setText(mlsLng+"");

        TextView gpsLatTextView = (TextView)v.findViewById(R.id.gpsLatTextView);
        gpsLatTextView.setText(gpsLat+"");

        TextView gpsLngTextView = (TextView)v.findViewById(R.id.gpsLngTextView);
        gpsLngTextView.setText(gpsLng+"");

        TextView distanceView = (TextView)v.findViewById(R.id.distanceTextView);
        distanceView.setText(fragment.record.getDistance() + "");
        TextView mlsLatTextView = (TextView)getView().findViewById(R.id.mlsLatTextView);
        mlsLatTextView.setText(mlsLat + "");*/
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        TextView mlsLngTextView = (TextView) view.findViewById(R.id.mlsLngTextView);
        mlsLngTextView.setText(record.mlsLng+"");


        TextView mlsLatTextView = (TextView) view.findViewById(R.id.mlsLatTextView);
        mlsLatTextView.setText(record.mlsLat+"");

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
