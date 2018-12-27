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
 * ItemTouchHelperInterface serves to pass messages from the custom ItemTouchHelper.Callback
 * class (ItemTouchHelperCallback) to the RecyclerViewAdapter
 */

public interface ItemTouchHelperInterface {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
