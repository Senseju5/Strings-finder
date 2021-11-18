# Strings-finder

    
Selection principle:

Strings are selected according to the simplest regexp: (for now only operators * and ?):

    character '*' - a sequence of any characters of unlimited length
    character '?' - any single character
    
Search should return lines that satisfy the mask.

For example:

    Mask *abc* selects all lines that contain 'abc', beginning and ending with any sequence of characters.
    Mask abc* selects all lines beginning with 'abc' and ending with any sequence of characters.
    Mask abc? selects all lines beginning with 'abc' and ending with any additional character.
    Mask abc selects all lines that are equal to this mask. 
