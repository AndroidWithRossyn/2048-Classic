package com.banrossyn.merge2048.org;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import com.banrossyn.merge2048.R;

public class ScoreBoardBuilder {

    private static final String TOP_SCORE = "TopScore";


    public static ArrayList<Score> createClassicArrayList(SharedPreferences sharedPreferences,String gameMode) {

        ArrayList<Score> scores = new ArrayList<>();

        scores.add(new Score("6x5",sharedPreferences.getLong(TOP_SCORE + gameMode + "6" + "5", 0), R.drawable.icon_trophy_empty));
        scores.add(new Score("5x4",sharedPreferences.getLong(TOP_SCORE + gameMode + "5" + "4", 0),R.drawable.icon_trophy_empty));
        scores.add(new Score("5x3",sharedPreferences.getLong(TOP_SCORE + gameMode + "5" + "3", 0),R.drawable.icon_trophy_empty));
        scores.add(new Score("4x3",sharedPreferences.getLong(TOP_SCORE + gameMode + "4" + "3", 0),R.drawable.icon_trophy_empty));
        scores.add(new Score("6x6",sharedPreferences.getLong(TOP_SCORE + gameMode + "6" + "6", 0),R.drawable.icon_trophy_empty));
        scores.add(new Score("5x5",sharedPreferences.getLong(TOP_SCORE + gameMode + "5" + "5", 0),R.drawable.icon_trophy_empty));
        scores.add(new Score("4x4",sharedPreferences.getLong(TOP_SCORE + gameMode + "4" + "4", 0),R.drawable.icon_trophy_empty));
        scores.add(new Score("3x3",sharedPreferences.getLong(TOP_SCORE + gameMode + "3" + "3", 0),R.drawable.icon_trophy_empty));


        Collections.sort(scores, new Comparator<Score>() {
                    @Override
                    public int compare(Score p1, Score p2) {
                        return (int) (p1.getScore() - p2.getScore());
                    }
                });
        Collections.reverse(scores);

        if (scores.get(0).getScore() != 0){
            scores.get(0).setIcon(R.drawable.icon_trophy_gold);
        }
        if (scores.get(1).getScore() != 0){
            scores.get(1).setIcon(R.drawable.icon_trophy_silver);
        }
        if (scores.get(2).getScore() != 0){
            scores.get(2).setIcon(R.drawable.icon_trophy_bronze);
        }


        return scores;
    }



    public static class ScoreAdapter extends BaseAdapter {
        private List<Score> scoreList;
        private Context context;

        public ScoreAdapter(List<Score> scoreList, Context context) {
            this.scoreList = scoreList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return scoreList.size();
        }

        @Override
        public Object getItem(int position) {
            return scoreList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                assert layoutInflater != null;
                convertView = layoutInflater.inflate(R.layout.score_line, parent , false);
            }
            Score score = scoreList.get(position);
            ImageView icon = convertView.findViewById(R.id.adapt_score_trophy);
            TextView tvBoardType = convertView.findViewById(R.id.adapt_tv_board_type);
            TextView tvScore = convertView.findViewById(R.id.adapt_tv_score);

            icon.setImageResource(score.getIcon());
            tvScore.setText(String.valueOf(score.score));


            tvBoardType.setText(score.boardType);

            return convertView;

        }
    }

    public static class Score {
        private String boardType;
        private long score;
        private int icon;

        public Score(String boardType, long score, int icon) {
            this.boardType = boardType;
            this.score = score;
            this.icon = icon;
        }

        public long getScore() {
            return score;
        }
        public int getIcon() {
            return icon;
        }
        public void setIcon(int icon) {
            this.icon = icon;
        }

    }
}
