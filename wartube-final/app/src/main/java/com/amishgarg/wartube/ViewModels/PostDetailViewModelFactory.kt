package com.amishgarg.wartube.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class PostDetailViewModelFactory(val postKey: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostDetailViewModel(postKey) as T
    }
}