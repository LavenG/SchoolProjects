BUILD  := build
STATIC := static

SOURCES := part1.c part2.c part3.c 

SOBJS := $(STATIC)/part1_harness.o \
		 $(STATIC)/part2_harness.o \
		 $(STATIC)/part3_harness.o \
		 $(STATIC)/part4_harness.o 

CC     := gcc
CFLAGS := -std=c99

OBJS :=	$(BUILD)/part1 \
	    $(BUILD)/part2 \
	    $(BUILD)/part3 \
	    $(BUILD)/part4


all: $(OBJS)
	@:

real_all: $(OBJS)

$(BUILD)/part%: part%.c $(SOBJS)
	@mkdir -p $(@D)
	$(CC) $(CFLAGS) -g -c part$*.c -o $(BUILD)/part$*.o
	$(CC) $(CFLAGS) $(STATIC)/part$*_harness.o $(BUILD)/part$*.o -lm -o $(BUILD)/part$*

clean-logs: always
	rm -f *.out

clean: always clean-logs
	rm -rf $(BUILD)

test:
	@echo $(MAKE) clean
	@$(MAKE) -s --no-print-directory clean
	@./test-lab

.PHONY: all always
