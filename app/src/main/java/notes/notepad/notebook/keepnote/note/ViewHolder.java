package notes.notepad.notebook.keepnote.note;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arnd.airdoc.R;

public class ViewHolder extends RecyclerView.ViewHolder {


    View view;
    public TextView titleview;
    public TextView dateview;
    public CardView color_shw;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        titleview = itemView.findViewById(R.id.tap_document);
        dateview = itemView.findViewById(R.id.tap_date);
        color_shw = itemView.findViewById(R.id.hm_clr);


        view = itemView;


    }
}
