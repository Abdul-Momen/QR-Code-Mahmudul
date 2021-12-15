package notes.notepad.notebook.keepnote.note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arnd.airdoc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class myadapter extends FirebaseRecyclerAdapter<Modelclass, myadapter.myviewholder> {
    public myadapter(@NonNull FirebaseRecyclerOptions<Modelclass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Modelclass model) {
        holder.name.setText(model.getT());
        holder.date.setText(model.getDt());

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder {

        TextView name,date;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nametext);
            date = (TextView) itemView.findViewById(R.id.date_txt);
        }
    }
}
