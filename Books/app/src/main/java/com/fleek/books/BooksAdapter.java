package com.fleek.books;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewholder>{
    ArrayList<Book> mBooks;
    public BooksAdapter(ArrayList<Book> books){
        this.mBooks = books;
    }

    @NonNull
    @Override
    public BooksViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        return new BooksViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksViewholder holder, int position) {
        Book book = mBooks.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public class BooksViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle, tvPublisher, tvAuthors, tvPublishedDate;

        public BooksViewholder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthors = (TextView) itemView.findViewById(R.id.tv_authors);
            tvPublisher = (TextView) itemView.findViewById(R.id.tv_publisher);
            tvPublishedDate = (TextView) itemView.findViewById(R.id.tv_publishedDate);
            itemView.setOnClickListener(this);
        }

        public void bind(Book book){
            tvTitle.setText(book.title);
            tvAuthors.setText(book.authors);
            tvPublisher.setText(book.publisher);
            tvPublishedDate.setText(book.publishedDate);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Book selectedBook = mBooks.get(position);
            Intent intent = new Intent(v.getContext(), BookDetails.class);
            intent.putExtra("Book", selectedBook);
            v.getContext().startActivity(intent);
        }
    }
}
