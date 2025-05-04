module pwm_servo (
    input clk,
    input [21:0] dutycycle, //input duty cycle value for the pwm
    output reg signal = 1'b0 //output signal value for the pwm
);
    parameter CLOCK_FREQ = 100_000_000; //100MHz clock frequency
    parameter PWM_PERIOD = 2_000_000; //2ms period for 50Hz at 100Mz clock
    reg [21:0] counter = 0; //22-bit counter for tracking time up to 2 million
    
    always @(posedge clk) begin
        if (counter >= PWM_PERIOD - 1) begin
            counter <= 0; //resetting counter after 1 cycle
        end else begin
            counter <= counter + 1; //incrementing counter on each clock cycle
        end

        // generates PWM signal. it is high if the counter is less than duty cycle
        if (counter < dutycycle)
            signal <= 1'b1;
        else
            signal <= 1'b0;
    end
endmodule
