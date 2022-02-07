package com.example.carpoolbuddy;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VehicleViewHolder extends RecyclerView.ViewHolder {
    TextView vehicleNameText;
    TextView ownerNameText;
    TextView capacityText;
    TextView priceText;
    ImageView addPersonIcon;

    public VehicleViewHolder(@NonNull View itemView, final VehicleAdapter.OnItemClickListener listener)
    {
        super(itemView);

        vehicleNameText = itemView.findViewById(R.id.vehicleNameView);
        ownerNameText = itemView.findViewById(R.id.ownerNameView);
        capacityText = itemView.findViewById(R.id.capacityView);
        priceText = itemView.findViewById(R.id.statusView);

        addPersonIcon = itemView.findViewById(R.id.addPerson);

        //go to chosen vehicle's VehicleProfileActivity when row is pressed

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

        //book a vehicle when addPersonIcon is clicked
        addPersonIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (listener != null)
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        listener.onAddClick(position);
                    }
                }
            }
        });
    }
}
