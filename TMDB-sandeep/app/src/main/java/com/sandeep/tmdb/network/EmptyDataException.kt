package com.sandeep.tmdb.network

import okhttp3.Headers

class EmptyDataException(val headers: Headers) : Exception()