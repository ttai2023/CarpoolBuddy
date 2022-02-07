package com.example.carpoolbuddy;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserVehiclesViewHolder extends RecyclerView.ViewHolder
{
    TextView vehicleNameText;
    TextView ownerNameText;
    TextView capacityText;
    TextView statusText;
    ImageView optionView;

    public UserVehiclesViewHolder(@NonNull View itemView, final UserVehiclesAdapter.OnItemClickListener listener)
    {
        super(itemView);

        vehicleNameText = itemView.findViewById(R.id.vehicleNameView);
        ownerNameText = itemView.findViewById(R.id.ownerNameView);
        capacityText = itemView.findViewById(R.id.capacityView);
        statusText = itemView.findViewById(R.id.statusView);
        optionView = itemView.findViewById(R.id.optionView);

        //if row is clicked, call onItemClick in UserVehiclesFragment

        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (listener != null)
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }
}
