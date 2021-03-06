package com.example.shopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopping.Element.ElementBoundary;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private int multipleChoice;
    private boolean withButton;
    boolean withCheckBox;
    private String buttonName;
    private Activity activity;
    private List<?> actions;
    private LayoutInflater inflater;

    private List<Integer> positionCheckedBox;

    public ListViewAdapter(Activity activity) {
        this.activity = activity;
    }

    public ListViewAdapter(Activity activity, List<?> actions, boolean withButton, String buttonName, boolean withCheckBox,
                           int multipleChoice) {
        this.multipleChoice = multipleChoice;
        this.withButton = withButton;
        this.withCheckBox = withCheckBox;
        this.buttonName = buttonName;
        this.activity = activity;
        this.actions = actions;

        positionCheckedBox = new ArrayList<>();

        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return actions.size();
    }

    @Override
    public Object getItem(int i) {
        return actions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.list_view_item, viewGroup, false);
            holder = new ViewHolder();

            holder.text = view.findViewById(R.id.text);
            holder.ivCheckBox = view.findViewById(R.id.ivCheckBox);
            holder.button = view.findViewById(R.id.info);

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (buttonName.matches("info")) {
                        ((MainActivity) activity).selectedInfo = (ElementBoundary) actions.get(position);
                        Intent intent = new Intent(activity, InfoActivity.class);
                        intent.putExtras(new Bundle());
                        activity.startActivity(intent);
                    } else {
                        ((MainActivity) activity).isCreating = false;
                        ((MainActivity) activity).selectedUpdate = (ElementBoundary) actions.get(position);
                        Intent intent = new Intent(activity, CreateUpdateElement.class);
                        intent.putExtras(new Bundle());
                        activity.startActivity(intent);
                    }
                }
            });

            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        String tempText;
        if (actions.get(0).getClass() == ElementBoundary.class) {
            ElementBoundary tempElementBoundary = (ElementBoundary) actions.get(position);
            tempText = tempElementBoundary.getName();
            if (tempElementBoundary.getType().equals("store"))
                tempText = tempElementBoundary.getName() + " in " + tempElementBoundary.getElementAttributes().get("mall");
        } else {
            tempText = (String) actions.get(position);
        }

        holder.button.setText(buttonName);
        if (!this.withButton)
            holder.button.setVisibility(View.GONE);
        if (withCheckBox) {
            holder.text.setVisibility(View.GONE);
            holder.ivCheckBox.setText(tempText);
        } else {
            holder.ivCheckBox.setVisibility(View.GONE);
            holder.text.setText(tempText);
        }

        holder.ivCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (positionCheckedBox.contains(position)) {
                    positionCheckedBox.remove((Object) position);
                    compoundButton.setChecked(false);
                    return;
                }

                if (multipleChoice == 1)
                    oneChoice(position, compoundButton);
                if (multipleChoice == 2)
                    twoChoice(position, compoundButton);
                if (multipleChoice == 0) // multiple choice
                    positionCheckedBox.add(position);
            }
        });

        return view;
    }

    public void oneChoice(int position, CompoundButton cb) {
        if (positionCheckedBox.size() == 0) {
            positionCheckedBox.add(position);
            cb.setChecked(true);
            return;
        }
        cb.setChecked(false);
        Toast.makeText(activity, "you can only choose one action", Toast.LENGTH_LONG).show();
    }

    public void twoChoice(int position, CompoundButton cb) {
        if (positionCheckedBox.size() < 2) {
            positionCheckedBox.add(position);
            cb.setChecked(true);
            return;
        }
        cb.setChecked(false);
        Toast.makeText(activity, "you can only choose two action", Toast.LENGTH_LONG).show();
    }

    public void setActions(List<?> actions) {
        this.actions = actions;
    }

    public List<Integer> getCheckedBox() {
        return positionCheckedBox;
    }

    class ViewHolder {

        TextView text;
        CheckBox ivCheckBox;
        Button button;

    }
}
