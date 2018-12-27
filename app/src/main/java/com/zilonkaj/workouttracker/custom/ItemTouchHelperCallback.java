package com.zilonkaj.workouttracker.custom;

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
 * ItemTouchHelperCallback overloads the ItemTouchHelper.Callback methods.
 * Also contains a reference to the parent RecyclerViewAdapter (cast as an ItemTouchHelperInterface)
 * so that when an ItemTouchHelper.Callback method is called, this class's overloaded methods can
 * pass that message to the RecyclerViewAdapter using the ItemTouchHelperInterface
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperInterface parentRecyclerViewAdapter;

    public ItemTouchHelperCallback(ItemTouchHelperInterface parentRecyclerViewAdapter)
    {
        this.parentRecyclerViewAdapter = parentRecyclerViewAdapter;
    }

    // Specifies which directions of drags/swipes are allowed. This implementation allows both
    // directions
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

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull
            RecyclerView.ViewHolder sourceViewHolder, @NonNull RecyclerView.ViewHolder
            targetViewHolder) {

        // Notify RecyclerViewAdapter that an item may have been moved
        return parentRecyclerViewAdapter.onItemMove(sourceViewHolder.getAdapterPosition(),
                targetViewHolder.getAdapterPosition());
    }

    // Works for both left & right swipes since ignoring direction parameter
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        parentRecyclerViewAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
