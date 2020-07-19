package com.fleek.books;

import android.content.Context;
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

    public class BooksViewholder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvPublisher, tvAuthors, tvPublishedDate;

        public BooksViewholder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthors = (TextView) itemView.findViewById(R.id.tv_authors);
            tvPublisher = (TextView) itemView.findViewById(R.id.tv_publisher);
            tvPublishedDate = (TextView) itemView.findViewById(R.id.tv_publishedDate);
        }

        public void bind(Book book){
            tvTitle.setText(book.title);
            String authors = "";
            int i = 0;
            for(String author:book.authors){
                authors += author;
                i++;
                if(i<book.authors.length){
                    authors += ", ";
                }
            }
            tvAuthors.setText(authors);
            tvPublisher.setText(book.publisher);
            tvPublishedDate.setText(book.publishedDate);
        }
    }
}
