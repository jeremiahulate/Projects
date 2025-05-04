module pwm_dc (
    input clk,
    input [7:0] dc_dutycycle, //input duty cycle value for the pwm
    output reg dc_signal = 1'b0 //output signal value for the pwm
);
    parameter PWM_PERIOD = 5000; //2ms period for 50Hz at 100Mz clock
    reg [12:0] counter = 0; //22-bit counter for tracking time up to 2 million
    
    always @(posedge clk) begin
        if (counter >= PWM_PERIOD - 1) begin
            counter <= 0; //resetting counter after 1 cycle
        end else begin
            counter <= counter + 1; //incrementing counter on each clock cycle
        end

        // generates PWM signal. it is high if the counter is less than duty cycle
        if (counter < (dc_dutycycle * PWM_PERIOD) / 255)
            dc_signal <= 1'b1;
        else
            dc_signal <= 1'b0;
    end
endmodule
