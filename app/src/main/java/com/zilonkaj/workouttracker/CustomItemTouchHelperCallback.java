package com.zilonkaj.workouttracker;

/*
 * Adapted from Paul Burke's Medium.com article:
 * https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * CustomItemTouchHelperCallback overloads the ItemTouchHelper.Callback methods.
 * Also contains a reference to the RecyclerViewAdapter (casted as a ItemTouchHelperAdapter)
 * so that when a method in this class is called, it can pass that message to the
 * RecyclerViewAdapter using the ItemTouchHelperAdapter class (in other words,
 * ItemTouchHelperAdapter serves to pass messages from this class to the RecyclerViewAdapter)
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class CustomItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter referenceToRecyclerViewAdapter;

    public CustomItemTouchHelperCallback(ItemTouchHelperAdapter referenceToRecyclerViewAdapter)
    {
        this.referenceToRecyclerViewAdapter = referenceToRecyclerViewAdapter;
    }

    /*
    specifies which directions of drags/swipes are allowed. this implementation
    allows both directions
    */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull
            RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    // notify RecyclerViewAdapter that an item may have been moved
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull
            RecyclerView.ViewHolder sourceViewHolder, @NonNull RecyclerView.ViewHolder
            targetViewHolder1) {

        referenceToRecyclerViewAdapter.onItemMove(sourceViewHolder.getAdapterPosition(),
                targetViewHolder1.getAdapterPosition());

        return true;
    }

    // works for both left & right swipes since ignoring direction param
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        referenceToRecyclerViewAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
