#!/usr/bin/env bash

_oh-my-mustache() {

    local DEFAULT_ARGS=("--help -h --verbose --version")
    local TEMPLATE_ARGS=("--template-file --template -t")
    local CONTEXT_ARGS=("--context-file --variable -v")

    COMPREPLY=()
    local cur=${COMP_WORDS[COMP_CWORD]}
    local prev=${COMP_WORDS[COMP_CWORD-1]}

    case "$prev" in
        "--context-file"|"--template-file")
            COMPREPLY=($(compgen -f ${cur}))
            return 0
        ;;
    esac

    COMPREPLY=($(compgen -W "${DEFAULT_ARGS} ${TEMPLATE_ARGS} ${CONTEXT_ARGS}" -- ${cur}))
    return 0
}

complete -F _oh-my-mustache oh-my-mustache
