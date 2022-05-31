package com.rw.colormemory;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.rw.colormemoryhelper.Score;
import com.rw.colormemoryhelper.ViewFlipperCustomClickEventListener;

import java.util.List;

/**
 * Created by Rakhita on 2/16/2018.
 */

public class ColorMemoryGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<SparseIntArray> mImageList;
    private int mPreviousItem = -1;
    private ViewFlipper mPrevViewFlipper;
    private int mRowHeight;
    private Score mCurrentScoreInstance;
    private ViewFlipperCustomClickEventListener mListener;
    private MediaPlayer mMediaPlayerCorrect;
    private MediaPlayer mMediaPlayerWrong;

    public ColorMemoryGridViewAdapter(Context context, List<SparseIntArray> imageList, int rowHeight) {
        this.mContext = context;
        this.mImageList = imageList;
        this.mRowHeight = rowHeight;
        this.mCurrentScoreInstance = Score.Instance();
        this.mMediaPlayerCorrect = MediaPlayer.create(context, R.raw.correct);
        this.mMediaPlayerWrong = MediaPlayer.create(context, R.raw.wrong);
    }

    public int getCount() {
        return mImageList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        final View grid;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.color_memory_grid_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.imageViewBack = grid.findViewById(R.id.imageView_Back);
            viewHolder.viewFlipperCard = grid.findViewById(R.id.viewFlipper);

            grid.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, mRowHeight));
            grid.setTag(viewHolder);
        } else {
            grid = convertView;
        }

        ViewHolder holder = (ViewHolder) grid.getTag();
        holder.viewFlipperCard.setInAnimation(mContext, R.anim.in_from_left);
        holder.viewFlipperCard.setOutAnimation(mContext, R.anim.out_to_left);

        SparseIntArray currentItem = mImageList.get(position);
        int key = currentItem.keyAt(0);
        holder.imageViewBack.setTag(key);

        int imageResourceId = currentItem.get(key);
        holder.imageViewBack.setImageResource(imageResourceId);

        holder.viewFlipperCard.setTag(key);
        if (holder.viewFlipperCard != null) {
            holder.viewFlipperCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    ((ViewFlipper) v).showNext();

                    ImageView currentImageViewFront = v.findViewById(R.id.imageView_Back);
                    int currentItem = (int) currentImageViewFront.getTag();
                    if (mPreviousItem == -1) {
                        mPreviousItem = currentItem;
                        mPrevViewFlipper = (ViewFlipper) v;
                        mPrevViewFlipper.setEnabled(false);
                    } else {
                        if (mPreviousItem != -1 && mPreviousItem == currentItem) {
                            disableGridView((GridView) parent);

                            Toast.makeText(mContext, R.string.itsAMatch, Toast.LENGTH_SHORT).show();
                            if(mMediaPlayerCorrect != null) mMediaPlayerCorrect.start();

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mPrevViewFlipper.setVisibility(View.INVISIBLE);
                                    v.setVisibility(View.INVISIBLE);
                                    mPreviousItem = -1;
                                    mPrevViewFlipper = null;

                                    mCurrentScoreInstance.plusCurrentScore();
                                    mCurrentScoreInstance.setMatchingCount(1);

                                    if (mListener != null)
                                        mListener.onViewFlipperCustomClickEvent();

                                    final Handler enableHandler = new Handler();
                                    enableHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            enableGridView((GridView) parent);
                                        }
                                    }, 1000);

                                }
                            }, 1000);

                        } else {
                            disableGridView((GridView) parent);

                            Toast.makeText(mContext, R.string.tryAgain, Toast.LENGTH_SHORT).show();
                            if(mMediaPlayerWrong != null) mMediaPlayerWrong.start();

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((ViewFlipper) v).showNext();
                                    mPrevViewFlipper.showNext();
                                    mPreviousItem = -1;
                                    mPrevViewFlipper = null;

                                    mCurrentScoreInstance.minusCurrentScore();

                                    if (mListener != null)
                                        mListener.onViewFlipperCustomClickEvent();

                                    final Handler enableHandler = new Handler();
                                    enableHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            enableGridView((GridView) parent);
                                        }
                                    }, 1000);

                                }
                            }, 1000);
                        }
                    }
                }
            });
        }
        return grid;
    }

    static class ViewHolder {
        public ImageView imageViewBack;
        public ViewFlipper viewFlipperCard;
    }

    public void setSomeEventListener(ViewFlipperCustomClickEventListener listener) {
        this.mListener = listener;
    }

    public void disableGridView(GridView gridView){
        for (int i = 0; i < gridView.getChildCount(); i++) {
            View view = gridView.getChildAt(i);
            ViewFlipper viewFlipper = view.findViewById(R.id.viewFlipper);
            viewFlipper.setEnabled(false);
        }
    }

    public void enableGridView(GridView gridView){
        for (int i = 0; i < gridView.getChildCount(); i++) {
            View view = gridView.getChildAt(i);
            ViewFlipper viewFlipper = view.findViewById(R.id.viewFlipper);
            viewFlipper.setEnabled(true);
        }
    }
}