#!/bin/bash
curl http://localhost:8080/generateWork\?id\=$1\&definition\=test$1 -H "X-B3-TraceId: $1" -H "X-B3-SpanId: $1" -H "X-Span-Name: SpanName$1" -H "X-Span-Export: true"
# curl http://localhost:8080/generateWork\?id\=$1\&definition\=test$1