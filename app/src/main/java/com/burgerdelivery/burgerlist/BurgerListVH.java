package com.burgerdelivery.burgerlist;

import android.view.View;
import android.widget.TextView;

import com.burgerdelivery.R;
import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.ui.recyclerview.CustomRecyclerViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

class BurgerListVH extends CustomRecyclerViewHolder {
    @BindView(R.id.sdvBurgerItemImage)
    SimpleDraweeView burgerImageSimpleDraweeView;

    @BindView(R.id.tvBurgerItemName)
    TextView burgerNameTextView;

    @BindView(R.id.tvBurgerItemDescription)
    TextView burgerDescriptionTextView;

    BurgerListVH(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    void bind(BurgerModel burgerModel) {
        burgerNameTextView.setText(burgerModel.getName());
        burgerDescriptionTextView.setText(burgerModel.getDescription());

        burgerImageSimpleDraweeView.setImageURI(burgerModel.getImageUrl());
    }
}
