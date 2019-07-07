package adpaterrecycler;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Ultimatecode.atmlocatorfinder.R;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private View view;
    private Context resultScreen;
    private ArrayList<HashMap<String, Object>> imagelist;
    private HashMap<String, Object> imagelistsub;
    private Dialog dialog;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public RelativeLayout imageclick;
        public ImageView imageall;
        public TextView imagename;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            imageclick = (RelativeLayout) view.findViewById(R.id.imageclick);
            imageall = (ImageView) view.findViewById(R.id.imageall);
            imagename = (TextView) view.findViewById(R.id.imagename);
        }
    }

    public ImageAdapter(Context context, ArrayList<HashMap<String, Object>> imglist) {
        resultScreen = context;
        imagelist = imglist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageAdapter.ViewHolder holder, final int position) {

        imagelistsub = imagelist.get(position);
        Bitmap bmp = BitmapFactory.decodeFile(imagelistsub.get("imagepath").toString());
        holder.imageall.setImageBitmap(bmp);
        holder.imagename.setText(imagelistsub.get("imagename").toString());

        holder.imageclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imagelistsub = imagelist.get(position);
                Bitmap bmp = BitmapFactory.decodeFile(imagelistsub.get("imagepath").toString());
                dialogyImage(bmp);

            }
        });
    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    private void dialogyImage(Bitmap imagebmp) {

        dialog = new Dialog(resultScreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_zoom);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        ImageView imagezoom = (ImageView) dialog.findViewById(R.id.imagezoom);
        imagezoom.setImageBitmap(imagebmp);

        PhotoViewAttacher mAttacher = new PhotoViewAttacher(imagezoom);

        ImageView closeimage = (ImageView) dialog.findViewById(R.id.closeimage);
        closeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}