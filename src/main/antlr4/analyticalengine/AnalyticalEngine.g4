/**
 * AnalyticalEngine.g4 - ANTLR4 grammar for the Analytical Engine language
 * 
 * Copyright 2014 Jeffrey Finkelstein.
 * 
 * This file is part of analyticalengine.
 * 
 * analyticalengine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * analyticalengine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * analyticalengine. If not, see <http://www.gnu.org/licenses/>.
 */
grammar AnalyticalEngine;

program
:
  (
    card LINESEP
  )*
;

card
:
  arithmetic
  | memory
  | combinatorial
  | action
  | curve
  | attendant
  | debug
  // | shortcombinatorial

;

//shortcombinatorial
//:
//  (
//    backstart
//    | cbackstart
//    | backend
//    | forwardstart
//    | cforwardstart
//    | alternation
//    | forwardend
//  )
//;

//backstart
//:
//  '('
//;
//
//cbackstart
//:
//  '(?'
//;
//
//backend
//:
//  ')'
//;
//
//forwardstart
//:
//  '{'
//;
//
//cforwardstart
//:
//  '{?'
//;
//
//alternation
//:
//  '}{'
//;
//
//forwardend
//:
//  '}'
//;

arithmetic
:
  (
    '+'
    | '/'
    | '-'
    | '*'
    | shift
  )
;

shift
:
  (
    '<'
    | '>'
  ) INT?
;

memory
:
  memoryCommand INT '\''?
  | numberCommand
;

numberCommand
:
  'N' INT WS SIGNEDNUMBER
;

combinatorial
:
  'C'
  (
    'B'
    | 'F'
  )
  (
    '?'
    | '+'
  ) INT
;

action
:
  (
    'H'
    | 'B'
    | 'P'
  )
;

curve
:
  'D'
  (
    '+'
    | '-'
    | 'X'
    | 'Y'
  )
;

attendant
:
  'A ' attendantCommand ' '
;

debug
:
  trace
  | lineComment
;

lineComment
:
  ' ' .
  | '.' .
;

trace
:
  'T'
  (
    '0'
    | '1'
  )
;

attendantCommand
:
  (
    'include cards'
    | 'include from library cards for'
    | 'set decimal places to'
    | 'write numbers as'
    | 'write numbers with decimal point'
    | 'write in rows'
    | 'write in columns'
    | 'write new line'
    | 'write annotation'
  )
;

SIGNEDNUMBER
:
  '+'? NUMBER
;

NUMBER
:
  INT
  | DOUBLE
;

DOUBLE
:
  INT '.' INT?
;

WS
:
  [ \t]+
;

memoryCommand
:
  (
    'L'
    | 'Z'
    | 'S'
  )
;

INT
:
  [0-9]+
;

LINESEP
:
  (
    '\n'
    | '\r\n'
    | '\r'
  )
;

T
:
  'T'
;
